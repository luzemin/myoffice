package com.myoffice.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myoffice.app.common.R;
import com.myoffice.app.model.domain.Admin;
import com.myoffice.app.model.request.AdminRequest;

public interface AdminService extends IService<Admin> {

    R verityPasswd(AdminRequest adminRequest);
}
