package com.es.phoneshop.core.phone.service;

import com.es.phoneshop.core.phone.dao.PhoneDao;
import com.es.phoneshop.core.phone.dao.PhoneDaoSelector;
import com.es.phoneshop.core.phone.dao.util.SortBy;
import com.es.phoneshop.core.phone.model.Phone;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {
    @Resource
    private PhoneDao phoneDao;

    @Override
    public int countPhones(String search) {
        return phoneDao.count(new PhoneDaoSelector().searching(search));
    }

    @Override
    public List<Phone> getPhoneList(String search, SortBy sortBy, Integer offset, Integer limit) {
        return phoneDao.findAll(new PhoneDaoSelector()
                .searching(search)
                .sortedBy(sortBy)
                .offset(offset)
                .limit(limit));
    }

    @Override
    public Optional<Phone> getPhone(Long id) {
        return phoneDao.get(id);
    }

    @Override
    public void save(Phone phone) {
        phoneDao.save(phone);
    }
}
