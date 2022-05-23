
declare @SQL varchar(max) = N'create procedure abu_populate_pc_cards_abu_I  @numOfRecords int, @bankBin char(6), @recordsCount int out
as

    declare @date_format  char(4)
    set @date_format =(select convert(char(4),getdate(),12))

   INSERT INTO [dbo].[pc_cards_abu]
           ([issuer_nr]
      ,[pan]
      ,[seq_nr]
      ,[card_program]
      ,[default_account_type]
      ,[card_status]
      ,[card_custom_state]
      ,[expiry_date]
      ,[hold_rsp_code]
      ,[track2_value]
      ,[track2_value_offset]
      ,[pvki_or_pin_length]
      ,[pvv_or_pin_offset]
      ,[pvv2_or_pin2_offset]
      ,[validation_data_question]
      ,[validation_data]
      ,[cardholder_rsp_info]
      ,[mailer_destination]
      ,[discretionary_data]
      ,[date_issued]
      ,[date_activated]
      ,[issuer_reference]
      ,[branch_code]
      ,[last_updated_date]
      ,[last_updated_user]
      ,[customer_id]
      ,[batch_nr]
      ,[company_card]
      ,[date_deleted]
      ,[pvki2_or_pin2_length]
      ,[extended_fields]
      ,[expiry_day]
      ,[from_date]
      ,[from_day]
      ,[contactless_disc_data]
      ,[dcvv_key_index]
      ,[pan_encrypted]
      ,[expiry_date_time])




SELECT top (@numOfRecords) PERCENT  pc.[issuer_nr]
      ,pc.[pan]
      ,pc.[seq_nr]
      ,pc.[card_program]
      ,pc.[default_account_type]
      ,pc.[card_status]
     ,pc.[card_custom_state]
     ,pc.[expiry_date]
     ,pc.[hold_rsp_code]
     ,pc.[track2_value]
     ,pc.[track2_value_offset]
     ,pc.[pvki_or_pin_length]
     ,pc.[pvv_or_pin_offset]
     ,pc.[pvv2_or_pin2_offset]
     ,pc.[validation_data_question]
     ,pc.[validation_data]
     ,pc.[cardholder_rsp_info]
     ,pc.[mailer_destination]
     ,pc.[discretionary_data]
     ,pc.[date_issued]
     ,pc.[date_activated]
     ,pc.[issuer_reference]
     ,pc.[branch_code]
      ,pc.[last_updated_date]
      ,pc.[last_updated_user]
      ,pc.[customer_id]
      ,pc.[batch_nr]
      ,pc.[company_card]
      ,pc.[date_deleted]
      ,pc.[pvki2_or_pin2_length]
      ,pc.[extended_fields]
      ,pc.[expiry_day]
      ,pc.[from_date]
      ,pc.[from_day]
      ,pc.[contactless_disc_data]
      ,pc.[dcvv_key_index]
      ,pc.[pan_encrypted]
      ,pc.[expiry_date_time]


  FROM [dbo].[pc_cards] as pc with (nolock)
  inner join pc_card_accounts as ca
  on pc.pan = ca.pan  and pc.seq_nr = ca.seq_nr

  where pc.date_deleted is null and expiry_date > @date_format and pc.card_status = 1
  and pc.hold_rsp_code is null  AND pc.card_program in (
	SELECT
		card_program from pc_card_programs with (nolock)
		WHERE left (card_prefix, 6) in (
			SELECT LTRIM(RTRIM(Split.a.value(''.'', ''VARCHAR(100)''))) ''Value''
			FROM
			(
			 SELECT CAST (''<M>'' + REPLACE(@bankBin, '','', ''</M><M>'') + ''</M>'' AS XML) AS Data
			) AS A
			CROSS APPLY Data.nodes (''/M'') AS Split(a)
		)
	)
   except
  SELECT [issuer_nr],[pan],[seq_nr],[card_program],[default_account_type]
      ,[card_status],[card_custom_state],[expiry_date],[hold_rsp_code]
      ,[track2_value],[track2_value_offset],[pvki_or_pin_length],[pvv_or_pin_offset]
      ,[pvv2_or_pin2_offset],[validation_data_question],[validation_data],[cardholder_rsp_info]
      ,[mailer_destination],[discretionary_data],[date_issued],[date_activated]
      ,[issuer_reference],[branch_code],[last_updated_date],[last_updated_user]
      ,[customer_id],[batch_nr],[company_card],[date_deleted],[pvki2_or_pin2_length]
      ,[extended_fields],[expiry_day],[from_date],[from_day],[contactless_disc_data]
      ,[dcvv_key_index],[pan_encrypted] ,[expiry_date_time] from pc_cards_abu;

  select @recordsCount = @@ROWCOUNT;'

execute(@SQL);
