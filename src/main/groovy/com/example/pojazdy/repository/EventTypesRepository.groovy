package com.example.pojazdy.repository

import com.example.pojazdy.exceptions.PojazdyException
import com.example.pojazdy.model.events.eventTypes.EventTypes
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

import javax.sql.DataSource
import java.sql.ResultSet
import java.sql.SQLException

@Repository
@CompileStatic
class EventTypesRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate
    private final EventMapper eventMapper

    @Autowired
    EventTypesRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
        this.eventMapper = new EventMapper()

    }

    private static Map<String, Object> setInsertParams(String partnerUUID, EventTypes eventTypes) {
        [
                PARTNER_UUID       : UUID.fromString(partnerUUID),
                CODE: eventTypes.code,
                NAME: eventTypes.name,
                IS_ACTIVE: eventTypes.isActive,
                CATEGORY: eventTypes.category,
        ] as Map<String, Object>
    }

    void insert(String partnerUUID, EventTypes eventTypes) {
        try {
            def params = setInsertParams(partnerUUID, eventTypes)
            jdbcTemplate.update(Queries.INSERT_INTO_EVENT_TYPES, params)
        } catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    List<EventTypes> findEventTypes(String partnerUUID) {
        try {
            def params = [PARTNER_UUID: UUID.fromString(partnerUUID)]
            def sqlQuery = Queries.SELECT_EVENT_TYPES
            jdbcTemplate.query(sqlQuery, params, eventMapper)
        }
        catch (DataAccessException e) {
            throw new PojazdyException(e)
        }
    }

    private class EventMapper implements RowMapper<EventTypes> {

        @Override
        EventTypes mapRow(ResultSet rs, int rowNum) throws SQLException {
            def event = new EventTypes(
                    code: rs.getString("CODE"),
                    name: rs.getString("NAME"),
                    category: rs.getString("CATEGORY"),
            )
            event
        }
    }
}
