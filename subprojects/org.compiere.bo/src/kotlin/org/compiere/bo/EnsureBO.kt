package org.compiere.bo

import org.compiere.model.I_C_Opportunity
import org.compiere.orm.DefaultModelFactory
import org.compiere.orm.IModelFactory
import org.compiere.process.SvrProcess
import org.compiere.product.MCurrency
import org.idempiere.common.util.DB
import org.idempiere.common.util.Env
import java.sql.Timestamp

class EnsureBO : SvrProcess() {
    var businessPartnerId : Int = 0
    var AD_CLIENT_ID = 0 //AD_Client_ID
    var AD_ORG_ID = 0 //AD_Org_ID

    override fun prepare() {
        for (para in parameter) {
            if ( para.parameterName == "BusinessPartnerId" ) {
                businessPartnerId = para.parameterAsInt
            } else if ( para.parameterName == "AD_Client_ID" ) {
                AD_CLIENT_ID = para.parameterAsInt
            } else if ( para.parameterName == "AD_Org_ID" ) {
                AD_ORG_ID = para.parameterAsInt
            } else println( "unknown parameter ${para.parameterName}" )
        }
    }

    private fun getBoId() : Int {
        val sql =
                """
select * from adempiere.C_Opportunity
where (c_bpartner_id = ?) -- params 1.
and ad_client_id IN (0, ?) and ( ad_org_id IN (0,?) or ? = 0) and isactive = 'Y' -- params 2..4
order by 1 desc
                """.trimIndent()

        val cnn = DB.getConnectionRO()
        val statement = cnn.prepareStatement(sql)
        statement.setInt(1, businessPartnerId)

        statement.setInt(2, AD_CLIENT_ID)
        statement.setInt(3, AD_ORG_ID)
        statement.setInt(4, AD_ORG_ID)
        val rs = statement.executeQuery()

        var oppId = 0

        while(rs.next()) {
            oppId = rs.getInt("c_opportunity_id")
        }
        return oppId
    }

    override fun doIt(): String {
        val pi = processInfo
        var oppId = getBoId()
        if ( oppId <= 0 ) {
            val opp = MOpportunity( ctx, 0, null )
            opp.expectedCloseDate = Timestamp(System.currentTimeMillis())
            opp.c_BPartner_ID = businessPartnerId
            opp.opportunityAmt = 0.toBigDecimal()
            opp.c_Currency_ID = MCurrency.get(ctx, "CZK").c_Currency_ID
            opp.c_SalesStage_ID = 1000000
            opp.probability = 0.toBigDecimal()
            opp.save()

            oppId = getBoId()
        }

        pi.serializableObject = oppId

        return "oppId:$oppId"
    }

}