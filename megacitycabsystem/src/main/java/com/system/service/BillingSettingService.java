package com.system.service;

import com.system.model.BillingSetting;
import com.system.dao.BillingSettingDAO;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillingSettingService {

    private BillingSettingDAO billingSettingDAO;
    private static final Logger logger = Logger.getLogger(BillingSettingService.class.getName());

    public BillingSettingService() {
        billingSettingDAO = new BillingSettingDAO();
    }

    public Map<String, Float> loadAllSettings() {
        return billingSettingDAO.loadAllSettings();
    }

    public BillingSetting getSettingByName(String settingName) {
        return billingSettingDAO.getSettingByName(settingName);
    }

    public boolean updateSetting(BillingSetting setting) {
        return billingSettingDAO.updateSetting(setting);
    }
}