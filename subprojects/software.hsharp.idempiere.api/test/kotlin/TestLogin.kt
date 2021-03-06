import org.junit.Assert
import org.idempiere.app.Login
import org.idempiere.app.Micro
import org.idempiere.common.db.Database
import org.junit.Before
import org.junit.Test
import pg.org.compiere.db.DB_PostgreSQL
import software.hsharp.api.helpers.jwt.ILogin
import software.hsharp.api.helpers.jwt.ILoginService
import software.hsharp.idempiere.api.servlets.jwt.LoginManager
import software.hsharp.idempiere.api.servlets.services.LoginService
import software.hsharp.idempiere.api.servlets.services.SystemService
import software.hsharp.idempiere.api.servlets.services.UserService
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestLogin {

    companion object {
        val gardenUser = LoginParams("GardenUser", "GardenUser", orgId = 50001, language="en-US")
        val fail = LoginParams("aaa", "aaa123")
    }

    data class LoginParams(
            override val loginName: String,
            override val password: String,
            override val clientId: Int? = null,
            override val roleId: Int? = null,
            override val orgId: Int? = null,
            override val warehouseId: Int? = null,
            override val language: String? = null
    ) : ILogin

    @Before
    fun prepare() {
        SystemService().setSystem(Micro())
        Database().setDatabase(DB_PostgreSQL())
        LoginService().setLoginUtility(Login())
        UserService().setUserService(org.compiere.bo.UserService())
    }

    @Test
    fun testLoginREST() {
        val loginUser: ILoginService = LoginManager()
        val result = loginUser.login(gardenUser)
        assertTrue(
            result.logged
        )
        assertTrue(
                result.token != ""
        )
    }

    @Test
    fun testLoginFailREST() {
        val loginUser: ILoginService = LoginManager()
        val result = loginUser.login(fail)
        assertFalse(
                result.logged
        )
    }

    @Test
    fun testBoth() {
        testLoginREST()
        testLoginFailREST()
    }
}