package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.UpdateUserInfoRequest;
import cc.rockbot.dds.dto.WxLoginRequest;
import cc.rockbot.dds.dto.WxLoginResponse;
import cc.rockbot.dds.service.WxService;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WxService wxService;

    @Test
    void testLoginWx_Success() throws Exception {
        // 准备测试数据
        WxLoginRequest request = new WxLoginRequest();
        request.setCode("test_code");

        WxLoginResponse response = WxLoginResponse.builder()
                .token("test_token")
                .wxid("test_wxid")
                .userName("测试用户")
                .userOrg("测试组织")
                .userPhone("13800138000")
                .status(0)
                .orgId("test_org_id")
                .build();

        // 模拟服务层行为
        when(wxService.login(anyString())).thenReturn(response);

        // 执行测试
        mockMvc.perform(post("/api/auth/login-wx")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test_token"))
                .andExpect(jsonPath("$.wxid").value("test_wxid"))
                .andExpect(jsonPath("$.userName").value("测试用户"))
                .andExpect(jsonPath("$.userOrg").value("测试组织"))
                .andExpect(jsonPath("$.userPhone").value("13800138000"))
                .andExpect(jsonPath("$.status").value(0))
                .andExpect(jsonPath("$.orgId").value("test_org_id"));
    }

    @Test
    void testLoginWx_InvalidRequest() throws Exception {
        // 准备测试数据
        WxLoginRequest request = new WxLoginRequest();
        request.setCode(""); // 空code，应该触发验证错误

        // 执行测试
        mockMvc.perform(post("/api/auth/login-wx")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRefreshToken_Success() throws Exception {
        // 准备测试数据
        String token = "test_token";
        String newToken = "new_test_token";

        // 模拟服务层行为
        when(wxService.refreshToken(anyString())).thenReturn(newToken);

        // 执行测试
        mockMvc.perform(post("/api/auth/refresh-token")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(newToken));
    }

    @Test
    void testRefreshToken_NoToken() throws Exception {
        // 执行测试
        mockMvc.perform(post("/api/auth/refresh-token"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUserInfo_Success() throws Exception {
        // 准备测试数据
        String token = "test_token";
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setUserName("新用户名");
        request.setUserOrg("新组织");
        request.setUserPhone("13900139000");
        request.setOrgId("new_org_id");

        WxLoginResponse response = WxLoginResponse.builder()
                .token("new_token")
                .wxid("test_wxid")
                .userName("新用户名")
                .userOrg("新组织")
                .userPhone("13900139000")
                .status(0)
                .orgId("new_org_id")
                .build();

        // 模拟服务层行为
        when(wxService.updateUserInfo(anyString(), any(UpdateUserInfoRequest.class))).thenReturn(response);

        // 执行测试
        mockMvc.perform(post("/api/auth/update-user-info")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("new_token"))
                .andExpect(jsonPath("$.userName").value("新用户名"))
                .andExpect(jsonPath("$.userOrg").value("新组织"))
                .andExpect(jsonPath("$.userPhone").value("13900139000"))
                .andExpect(jsonPath("$.orgId").value("new_org_id"));
    }

    @Test
    void testUpdateUserInfo_NoToken() throws Exception {
        // 准备测试数据
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setUserName("新用户名");

        // 执行测试
        mockMvc.perform(post("/api/auth/update-user-info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUserInfo_InvalidRequest() throws Exception {
        // 准备测试数据
        String token = "test_token";
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        // 不设置任何字段，应该触发验证错误

        // 执行测试
        mockMvc.perform(post("/api/auth/update-user-info")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(request)))
                .andExpect(status().isBadRequest());
    }
} 