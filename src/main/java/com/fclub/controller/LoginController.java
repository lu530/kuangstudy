package com.fclub.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fclub.cache.TemporaryCache;
import com.fclub.cache.VerifyImageCache;
import com.fclub.constant.ConstString;
import com.fclub.dto.PictureTemplagtesCutDto;
import com.fclub.pojo.User;
import com.fclub.pojo.UserInfo;
import com.fclub.service.InviteService;
import com.fclub.service.UserInfoService;
import com.fclub.service.UserService;
import com.fclub.utils.JavaMailUtils;
import com.fclub.utils.KuangUtils;
import com.fclub.utils.VerifyImageUtil;
import com.fclub.vo.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class LoginController {

    @Autowired
    InviteService inviteService;
    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;

    @Autowired
    private VerifyImageCache verifyImageCache;

    @Autowired
    private TemporaryCache temporaryCache;

    @GetMapping({"/","/index"})
    public String index(){
        return "index";
    }

    @GetMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @GetMapping("/toLogin/{message}")
    public String toLogin(@PathVariable("message") String message,Model model){
        model.addAttribute("message",message);
        return "login";
    }

    @GetMapping("/register")
    public String toRegister(){
        return "register";
    }

    // 注册业务
    @RequestMapping("/register")
    public String register(RegisterForm registerForm,Model model){
        KuangUtils.print("注册表单信息："+registerForm.toString());
        // 表单密码重复判断
        if (!registerForm.getPassword().equals(registerForm.getRepassword())){
            model.addAttribute("registerMsg","密码输入有误");
            return "register";
        }
        // 用户名已存在
        User hasUser = userService.getOne(new QueryWrapper<User>().eq("username", registerForm.getUsername()));
        if (hasUser!=null){
            model.addAttribute("registerMsg","用户名已存在");
            return "register";
        }

        String mailCode = temporaryCache.getMailCode(registerForm.getEmail());
        if(!mailCode.equals(registerForm.getCode())){
            model.addAttribute("registerMsg","验证码不正确");
            return "register";
        }
      /*  // 验证邀请码
        Invite invite = inviteService.getOne(new QueryWrapper<Invite>().eq("code", registerForm.getCode()));
        if (invite==null){
            model.addAttribute("registerMsg","邀请码不存在");
            return "register";
        }
            // 邀请码存在，判断邀请码是否有效
        if (invite.getStatus()==1){
            model.addAttribute("registerMsg","邀请码已被使用");
            return "register";
        }
                // 激活邀请码
        invite.setActiveTime(KuangUtils.getTime());
        invite.setStatus(1);
        invite.setUid(user.getUid());
        inviteService.updateById(invite);

        */
        // 构建用户对象
        User user = new User();
        user.setUid(KuangUtils.getUuid()); // 用户唯一id
        user.setRoleId(2);
        user.setUsername(registerForm.getUsername());
        // 密码加密
        String bCryptPassword = new BCryptPasswordEncoder().encode(registerForm.getPassword());
        user.setPassword(bCryptPassword);
        user.setGmtCreate(KuangUtils.getTime());
        user.setLoginDate(KuangUtils.getTime());
        // 保存对象！
        userService.save(user);
        KuangUtils.print("新用户注册成功："+user);

        UserInfo userInfo = new UserInfo();
        userInfo.setUid(user.getUid());
        userInfo.setEmail(registerForm.getEmail());
        // todo: 用户信息
        userInfoService.save(userInfo);
        model.addAttribute("message","注册成功，请登录！");
        // 注册成功，重定向到登录页面
       // return "redirect:/toLogin/" + "注册成功，请登录！";
        return "login";

    }

    /**
     * 跳转到图片验证界面
     * @return 图片验证界面
     */
    @RequestMapping("imgValidate")
    public String toImgValidate(ModelMap map, String email){
        map.addAttribute("telephone",email);
        return "common/imageValidate";
    }

    @RequestMapping("createImgValidate")
    @ResponseBody
    public Map<String,Object> createImgValidate(String email){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Integer templateNum = new Random().nextInt(4) + 1;
            Integer targetNum = new Random().nextInt(20) + 1;
            File templateFile = ResourceUtils.getFile("classpath:static/images/template/"+templateNum+".png");
            File targetFile = ResourceUtils.getFile("classpath:static/images/target/"+targetNum+".jpg");
            PictureTemplagtesCutDto pictureTemplagtesCutDto = VerifyImageUtil.pictureTemplatesCut(templateFile, targetFile,
                    ConstString.IMAGE_TYPE_PNG, ConstString.IMAGE_TYPE_JPG);
            // 将生成的偏移位置信息设置到redis中
            String key = ConstString.WEB_VALID_IMAGE_PREFIX + email;
            Integer mailCode = verifyImageCache.getVerifyImage(key);
            if(null != mailCode && mailCode > 0){
                verifyImageCache.deleteVerifyImage(key);
            }
            verifyImageCache.putVerifyImage(key, pictureTemplagtesCutDto.getX());
            System.out.println(" verifyImageCache key " + key + " value:" + verifyImageCache.getVerifyImage(key) + " pictureTemplagtesCutDto.getX() " + pictureTemplagtesCutDto.getX());
            result.put("status", 200);
            result.put("data", pictureTemplagtesCutDto.getPictureMap());
            return result;
        } catch (Exception e) {
            result.put("status", 504);
            e.printStackTrace();
            return result;
        }
    }

    /**
     * @return 图片验证界面
     */
    @ResponseBody
    @RequestMapping("checkImgValidate")
    public Map<String,Object> checkImgValidate(String telephone, int offsetHorizontal) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        String key = ConstString.WEB_VALID_IMAGE_PREFIX + telephone;
        Integer verifyImage = verifyImageCache.getVerifyImage(key);
        System.out.println("key:" + key + " offsetHorizontal:" + offsetHorizontal + " verifyImage :" + verifyImage);
        if(null !=verifyImage && Math.abs(verifyImage - offsetHorizontal) < 2){//这个时候就要发送邮箱验证码了
            JavaMailUtils.setUserName(telephone);
            JavaMailUtils.setReceiveMailAccount(telephone);
            JavaMailUtils.send();
            System.out.println("JavaMailUtils.code:"+JavaMailUtils.code);
            temporaryCache.putMailCode(telephone, JavaMailUtils.code);
            result.put("status", 200);
            result.put("info"," 验证通过！请到您的邮箱里获取验证码！");
        }
        return result;
    }

}
