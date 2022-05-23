declare @SQL varchar(max) =  N'CREATE TYPE TVPCardUpdatesTableType AS TABLE
(
id BIGINT,
account_chain VARCHAR(66),
account_hierarchy INT,
 upload_status VARCHAR(2)
)
'

execute(@SQL);
