declare @SQL varchar(max) =  N'CREATE PROCEDURE [dbo].[get_closed_cards_encrypted]   @bins VARCHAR(200), @page_num INT,
  @page_size INT AS
SELECT
  COUNT(*) AS count
FROM
  pc_cards_abu INNER JOIN pc_card_accounts
      ON
        pc_cards_abu.pan = pc_card_accounts.pan
		and pc_cards_abu.seq_nr = pc_card_accounts.seq_nr
      WHERE
         (pc_cards_abu.card_status != ''1''
            OR pc_cards_abu.hold_rsp_code IN (''41'',''43'',''45'')
            OR pc_cards_abu.expiry_date <= (SELECT convert(char(4),getdate(),12)))
            AND pc_cards_abu.closed = 0
            AND pc_cards_abu.account_hierarchy IS NOT NULL
            AND pc_cards_abu.card_program in (
				SELECT
				card_program from pc_card_programs
				WHERE left (card_prefix, 6) in (
					SELECT LTRIM(RTRIM(Split.a.value(''.'', ''VARCHAR(100)''))) ''Value''
					FROM
					(
					 SELECT CAST (''<M>'' + REPLACE(@bins, '','', ''</M><M>'') + ''</M>'' AS XML) AS Data
					) AS A
					CROSS APPLY Data.nodes (''/M'') AS Split(a)
				)
			)
SELECT
        pc_cards_abu.id,
        pc_cards_abu.pan_encrypted as pan,
        pc_cards_abu.expiry_date,
        pc_cards_abu.account_hierarchy
FROM
  pc_cards_abu INNER JOIN pc_card_accounts
      ON
        pc_cards_abu.pan = pc_card_accounts.pan
		and pc_cards_abu.seq_nr = pc_card_accounts.seq_nr
      WHERE
        (pc_cards_abu.card_status != ''1''
           OR pc_cards_abu.hold_rsp_code IN (''41'',''43'',''45'')
           OR pc_cards_abu.expiry_date <= (SELECT convert(char(4),getdate(),12)))
           AND pc_cards_abu.closed = 0
           AND pc_cards_abu.account_hierarchy IS NOT NULL
           AND pc_cards_abu.card_program in (
				SELECT
				card_program from pc_card_programs
				WHERE left (card_prefix, 6) in (
					SELECT LTRIM(RTRIM(Split.a.value(''.'', ''VARCHAR(100)''))) ''Value''
					FROM
					(
					 SELECT CAST (''<M>'' + REPLACE(@bins, '','', ''</M><M>'') + ''</M>'' AS XML) AS Data
					) AS A
					CROSS APPLY Data.nodes (''/M'') AS Split(a)
				)
			)
ORDER BY
  id OFFSET @page_size * (@page_num - 1) ROWS FETCH NEXT @page_size ROWS ONLY'

execute(@SQL);
