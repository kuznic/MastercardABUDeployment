declare @SQL varchar(max) =  N'CREATE PROCEDURE update_closed_card_status @updates TVPClosedCardUpdatesTableType READONLY
AS
            UPDATE pc_cards_abu
            SET    closed = u.closed
            FROM   @updates u
            WHERE  u.id = pc_cards_abu.id
'

execute(@SQL);
