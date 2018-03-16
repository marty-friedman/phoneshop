package com.es.core.model.phone.dao;

import com.es.core.model.PhoneResultSetExtractor;
import com.es.core.model.SQLQueries;
import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JdbcPhoneDao implements PhoneDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Phone> get(final Long key) {
        List<Phone> phoneList = jdbcTemplate.query(SQLQueries.GET_PHONE_BY_ID, new PhoneResultSetExtractor(), key);
        if (phoneList.isEmpty())
            return Optional.empty();
        return Optional.of(phoneList.get(0));
    }

    public void save(final Phone phone) {
        if (phone.getId() == null)
            insert(phone);
        else
            update(phone);
    }

    private void insert(Phone phone) {
        SqlParameterSource source = new MapSqlParameterSource(getParameters(phone));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQLQueries.INSERT_PHONE, source, keyHolder);
        phone.setId(keyHolder.getKey().longValue());
        insertColorsForPhone(phone.getId(), phone.getColors());
    }

    private void update(Phone phone) {
        deleteColorsForPhone(phone.getId());
        insertColorsForPhone(phone.getId(), phone.getColors());
        namedParameterJdbcTemplate.update(SQLQueries.UPDATE_PHONE, getParameters(phone));
    }

    public List<Phone> findAll(int offset, int limit) {
        return jdbcTemplate.query(SQLQueries.SELECT_PHONES, new PhoneResultSetExtractor(), offset, limit);
    }

    private void deleteColorsForPhone(long phoneId) {
        jdbcTemplate.update(SQLQueries.DELETE_COLORS_BY_PHONE_ID, phoneId);
    }

    private void insertColorsForPhone(long phoneId, Set<Color> colors) {
        if (colors.isEmpty())
            return;
        List<Object[]> batchArgs = colors.stream()
                .map(color -> new Object[] {phoneId, color.getId()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(SQLQueries.INSERT_COLOR_FOR_PHONE_ID, batchArgs);
    }

    private Map<String, Object> getParameters(Phone phone) {
        return Stream.of(
                new AbstractMap.SimpleEntry<>("id", phone.getId()),
                new AbstractMap.SimpleEntry<>("brand", phone.getBrand()),
                new AbstractMap.SimpleEntry<>("model", phone.getModel()),
                new AbstractMap.SimpleEntry<>("price", phone.getPrice()),
                new AbstractMap.SimpleEntry<>("displaySizeInches", phone.getDisplaySizeInches()),
                new AbstractMap.SimpleEntry<>("weightGr", phone.getWeightGr()),
                new AbstractMap.SimpleEntry<>("lengthMm", phone.getLengthMm()),
                new AbstractMap.SimpleEntry<>("widthMm", phone.getWidthMm()),
                new AbstractMap.SimpleEntry<>("heightMm", phone.getHeightMm()),
                new AbstractMap.SimpleEntry<>("announced", phone.getAnnounced()),
                new AbstractMap.SimpleEntry<>("deviceType", phone.getDeviceType()),
                new AbstractMap.SimpleEntry<>("os", phone.getOs()),
                new AbstractMap.SimpleEntry<>("displayResolution", phone.getDisplayResolution()),
                new AbstractMap.SimpleEntry<>("pixelDensity", phone.getPixelDensity()),
                new AbstractMap.SimpleEntry<>("displayTechnology", phone.getDisplayTechnology()),
                new AbstractMap.SimpleEntry<>("backCameraMegapixels", phone.getBackCameraMegapixels()),
                new AbstractMap.SimpleEntry<>("frontCameraMegapixels", phone.getFrontCameraMegapixels()),
                new AbstractMap.SimpleEntry<>("ramGb", phone.getRamGb()),
                new AbstractMap.SimpleEntry<>("internalStorageGb", phone.getInternalStorageGb()),
                new AbstractMap.SimpleEntry<>("batteryCapacityMah", phone.getBatteryCapacityMah()),
                new AbstractMap.SimpleEntry<>("talkTimeHours", phone.getTalkTimeHours()),
                new AbstractMap.SimpleEntry<>("standByTimeHours", phone.getStandByTimeHours()),
                new AbstractMap.SimpleEntry<>("bluetooth", phone.getBluetooth()),
                new AbstractMap.SimpleEntry<>("positioning", phone.getPositioning()),
                new AbstractMap.SimpleEntry<>("imageUrl", phone.getImageUrl()),
                new AbstractMap.SimpleEntry<>("description", phone.getDescription())
        ).collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
    }
}