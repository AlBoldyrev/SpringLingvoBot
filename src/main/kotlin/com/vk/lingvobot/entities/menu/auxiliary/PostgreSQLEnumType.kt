package com.vk.lingvobot.entities.menu.auxiliary

import com.vk.lingvobot.entities.menu.MenuLevel
import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.type.EnumType
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Types


class PostgreSQLEnumType : EnumType<MenuLevel>() {
    @Throws(HibernateException::class, SQLException::class)

    override fun nullSafeSet(
        st: PreparedStatement,
        value: Any?,
        index: Int,
        session: SharedSessionContractImplementor?
    ) { //        super.nullSafeSet(st, value, index, session);
        if (value == null) {
            st.setNull(index, Types.OTHER)
        } else {
            st.setObject(index, (value as MenuLevel), Types.OTHER)
        }
    }
}