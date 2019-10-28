package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.MenuService;
import com.itheima.health.service.RoleService;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @Reference
    private RoleService roleService;

    @Reference
    private MenuService menuService;

    @RequestMapping("/getUsernameAndMenu")
    public Result getUsername() {
        try{
            User user1 = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user1.getUsername();
            com.itheima.health.pojo.User user2 = userService.findUserByUsername(username);
            Set<Role> roles = roleService.findRoleByUserId(user2.getId());
            LinkedHashSet<Menu> menuSet = new LinkedHashSet<>();
            if (roles != null && roles.size() > 0) {
                for (Role role : roles) {
                    LinkedHashSet<Menu> menus = menuService.findMenuByRoleId(role.getId());
                    if (menus != null && menus.size() > 0) {
                        for (Menu menu : menus) {
                            List<Menu> childrenList = menuService.findChildrenByPriority(menu.getId());
                            menu.setChildren(childrenList);
                        }
                        menuSet.addAll(menus);
                    }
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("menuList", menuSet);
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, map);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
