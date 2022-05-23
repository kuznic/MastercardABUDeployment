
IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[get_all_active_cards]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[get_all_active_cards];


IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[get_all_active_cards_encrypted]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[get_all_active_cards_encrypted];



IF EXISTS (SELECT * FROM   sys.objects WHERE  object_id = OBJECT_ID(N'[dbo].[SplitString]') AND type IN ( N'FN', N'IF', N'TF', N'FS', N'FT' ))

        DROP FUNCTION [dbo].[SplitString];


IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[get_all_chained_cards]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[get_all_chained_cards];

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[get_all_chained_cards_encrypted]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[get_all_chained_cards_encrypted];

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[get_closed_cards]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[get_closed_cards];

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[get_closed_cards_encrypted]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[get_closed_cards_encrypted];


IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[get_cards_to_delete]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[get_cards_to_delete];

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[get_cards_to_delete_encrypted]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[get_cards_to_delete_encrypted];


IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[check_for_abu_bins]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[check_for_abu_bins] ;

IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[abu_populate_pc_cards_abu_I]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[abu_populate_pc_cards_abu_I] ;


IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[abu_populate_pc_cards_abu_N]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[abu_populate_pc_cards_abu_N] ;

 IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[update_status_chain_and_hierarchy]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[update_status_chain_and_hierarchy] ;

        IF EXISTS (SELECT * FROM dbo.sysobjects WHERE id = object_id (N'[dbo].[update_closed_card_status]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)

        DROP PROCEDURE [dbo].[update_closed_card_status] ;



       IF type_id('TVPClosedCardUpdatesTableType') IS NOT NULL
            DROP TYPE TVPClosedCardUpdatesTableType;

IF type_id('TVPCardUpdatesTableType') IS NOT NULL
            DROP TYPE TVPCardUpdatesTableType;





