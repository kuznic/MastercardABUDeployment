declare @SQL varchar(max) =  N'CREATE PROCEDURE update_status_chain_and_hierarchy  @updates TVPCardUpdatesTableType READONLY
AS
            UPDATE pc_cards_abu
            SET    account_chain = u.account_chain,
                   account_hierarchy = u.account_hierarchy,
             upload_status = u.upload_status
            FROM   @updates u
            WHERE  u.id = pc_cards_abu.id
'

execute(@SQL);
