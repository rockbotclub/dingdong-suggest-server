package cc.rockbot.dds;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * 基础测试类
 * 所有测试类都应该继承这个类，以继承通用的测试配置
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
public abstract class BaseTest {
    // 可以在这里添加通用的测试工具方法或配置
} 