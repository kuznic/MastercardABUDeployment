declare @SQL varchar(max) =  N' CREATE TYPE  TVPClosedCardUpdatesTableType AS TABLE
(
id BIGINT,
closed BIT
)
'

execute(@SQL);
