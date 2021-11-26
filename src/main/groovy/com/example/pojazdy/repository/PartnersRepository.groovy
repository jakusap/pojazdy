package com.example.pojazdy.repository

import com.example.pojazdy.exceptions.PojazdyException
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import com.example.pojazdy.model.Partner


import javax.sql.DataSource
import java.sql.ResultSet
import java.sql.SQLException

/**
 *
 * @author Jakub Sapiński
 * */
@Repository
@CompileStatic
class PartnersRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate
    private final PartnerMapper partnerMapper

    @Autowired
    PartnersRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
        this.partnerMapper = new PartnerMapper()
    }

    Partner findPartnerByUUID(String partnerUUID) {

        try {
            jdbcTemplate.queryForObject(Queries.SELECT_PARTNER_BY_UUID, [PARTNER_UUID: UUID.fromString(partnerUUID)], partnerMapper)
        } catch (EmptyResultDataAccessException ignored) {
            null
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    String update(Partner partner) {
        try {
            def params = setInsertParams(partner)
            jdbcTemplate.update(Queries.UPDATE_PARTNER_BY_UUID, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
        partner.uuid
    }

    String add(Partner partner) {
        partner.uuid = UUID.randomUUID().toString()
        try {
            def params = setInsertParams(partner)
            params.ACS_ID = partner.acsId
            jdbcTemplate.update(Queries.INSERT_INTO_PARTNERS, params)
            jdbcTemplate.update(Queries.INSERT_INTO_USER_PARTNERS, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
        partner.uuid
    }

    Partner findPartnerByAcsId(String acsId) {
        try {
            jdbcTemplate.queryForObject(Queries.SELECT_PARTNER_BY_ACS_ID, [ACS_ID: acsId], partnerMapper)
        } catch (EmptyResultDataAccessException ignored) {
            null
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    private static Map<String, Object> setInsertParams(Partner partner) {
        [
                PARTNER_UUID                : UUID.fromString(partner.uuid),
                NAME                        : partner.name,
                EMAIL                       : partner.email,
                PHONE_NUMBER                : partner.phoneNumber,
        ] as Map<String, Object>
    }

    private class PartnerMapper implements RowMapper<Partner> {
        @Override
        Partner mapRow(ResultSet rs, int rowNum) throws SQLException {
            new Partner(
                    uuid: rs.getString("PARTNER_UUID"),
                    name: rs.getString("NAME"),
                    email: rs.getString("EMAIL"),
                    phoneNumber: rs.getString("PHONE_NUMBER"),
                    description: rs.getString("DESCRIPTION"),
            )
        }
    }
}