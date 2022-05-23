declare @SQL varchar(max) =  N'CREATE PROCEDURE [dbo].[get_cards_to_delete_encrypted] @bins VARCHAR(200), @page_num INT,
  @page_size INT AS

  SELECT COUNT(*) AS count
    FROM pc_cards_abu
    WHERE upload_status = ''1''
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
  SELECT pan_encrypted as pan
    FROM pc_cards_abu
    WHERE upload_status = ''1''
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
