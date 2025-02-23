package com.system.service;

import com.system.model.Admin;
import com.system.dao.AdminDAO;
import java.util.List;

public class AdminService {
    
    private AdminDAO adminDAO;

    public AdminService() {
        this.adminDAO = new AdminDAO();
    }

    public List<Admin> getAllAdmins() {
        return adminDAO.getAllAdmins();
    }

    public Admin getAdminById(int adminId) {
        return adminDAO.getAdminById(adminId);
    }

    public boolean createAdmin(Admin admin) {
        return adminDAO.createAdmin(admin);
    }

    public boolean deleteAdmin(int adminId) {
        return adminDAO.deleteAdmin(adminId);
    }
}
