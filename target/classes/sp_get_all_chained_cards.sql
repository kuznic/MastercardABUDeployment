

declare @SQL varchar(max) = N'CREATE PROCEDURE [dbo].[get_all_chained_cards] @bins VARCHAR(200), @page_num INT,
@page_size INT AS

SELECT
COUNT(*) AS count
FROM
  pc_cards_abu INNER JOIN pc_card_accounts
ON
  pc_cards_abu.pan = pc_card_accounts.pan
  and pc_cards_abu.seq_nr = pc_card_accounts.seq_nr
WHERE
(
  pc_cards_abu.card_status = ''1''
  AND pc_cards_abu.hold_rsp_code IS NULL
  AND pc_cards_abu.account_hierarchy IS NULL
  AND pc_cards_abu.expiry_date > (SELECT  convert(char(4), getdate(), 12))
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
)
;WITH old_card_ids(date_activated, account_id) AS
(
  SELECT
      MAX(pc_cards_abu.date_activated),
      pc_card_accounts.account_id
  FROM
      pc_cards_abu INNER JOIN pc_card_accounts
  ON
      pc_cards_abu.pan = pc_card_accounts.pan
	  and pc_cards_abu.seq_nr = pc_card_accounts.seq_nr

  WHERE
      (
          (
              pc_cards_abu.card_status != ''1''
              OR pc_cards_abu.hold_rsp_code IN (''41'',''43'',''45'')
              OR pc_cards_abu.expiry_date <= (SELECT  convert(char(4), getdate(), 12))
          )
          AND pc_cards_abu.account_hierarchy IS NOT NULL
      )
  GROUP BY pc_card_accounts.account_id
)

SELECT DISTINCT
  *
FROM
  (
      SELECT
          id,
          pc_cards_abu.pan,
          pc_cards_abu.expiry_date,
          pc_cards_abu.card_program,
          pc_card_accounts.account_id

      FROM
          pc_cards_abu with (nolock) INNER JOIN pc_card_accounts
  ON
      pc_cards_abu.pan = pc_card_accounts.pan
	  and pc_cards_abu.seq_nr = pc_card_accounts.seq_nr
      WHERE
	  (
		  pc_cards_abu.card_status = ''1''
		  AND pc_cards_abu.hold_rsp_code IS NULL
		  AND pc_cards_abu.account_hierarchy IS NULL
		  AND pc_cards_abu.expiry_date > (SELECT  convert(char(4), getdate(), 12))
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
		)
  )
  new_cards
LEFT JOIN
  (
      SELECT
          pc_cards_abu.pan old_pan,
          pc_cards_abu.expiry_date old_expiry_date,
          pc_cards_abu.card_program old_card_program,
          pc_cards_abu.account_hierarchy old_account_hierarchy,
          pc_card_accounts.account_id
      FROM
          pc_cards_abu with (nolock) INNER JOIN pc_card_accounts
  ON
      pc_cards_abu.pan = pc_card_accounts.pan
	  and pc_cards_abu.seq_nr = pc_card_accounts.seq_nr
      JOIN old_card_ids
      ON
          pc_card_accounts.account_id = old_card_ids.account_id
          AND pc_cards_abu.date_activated = old_card_ids.date_activated
  )
  old_cards
ON new_cards.account_id = old_cards.account_id
where ((old_account_hierarchy is null and old_pan is null) OR (old_account_hierarchy is not null and old_pan is not null))
ORDER BY
    id OFFSET @page_size * (@page_num - 1) ROWS FETCH NEXT @page_size ROWS ONLY
'

execute(@SQL);