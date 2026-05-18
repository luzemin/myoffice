package com.myoffice.app.config;

import com.myoffice.app.constant.Constants;
import org.apache.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * 应用启动时，为种子数据中的任务生成对应的模板文件。
 * 仅当文件目录不存在时才创建，不会覆盖已有文件。
 */
@Component
public class SeedFileInitializer implements ApplicationRunner {

    private final Logger logger = Logger.getLogger(getClass());

    /** 种子任务 fileId → 文件名的映射（与 data.sql 保持一致） */
    private static final String[][] SEED_TASKS = {
            {"seed-task-1",  "Q2汇报.docx",       "docx"},
            {"seed-task-2",  "销售统计.xlsx",      "xlsx"},
            {"seed-task-3",  "产品宣讲.pptx",      "pptx"},
            {"seed-task-4",  "合同模板2026.docx",   "docx"},
            {"seed-task-5",  "客户问卷.docx",      "docx"},
            {"seed-task-6",  "对账单Q1.xlsx",      "xlsx"},
            {"seed-task-7",  "周报模板.docx",      "docx"},
            {"seed-task-8",  "OKR复盘.pptx",       "pptx"},
            {"seed-task-9",  "研发进度.xlsx",      "xlsx"},
            {"seed-task-10", "项目存档.docx",      "docx"},
    };

    @Override
    public void run(ApplicationArguments args) {
        for (String[] seed : SEED_TASKS) {
            String fileId = seed[0];
            String fileName = seed[1];
            String format = seed[2];

            File dir = new File(Constants.FILE_DIR + fileId);
            if (dir.exists() && dir.listFiles() != null && dir.listFiles().length > 0) {
                continue; // 已有文件，跳过
            }

            try {
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String templatePath = Constants.FILE_TEMPLATE_PATH + Constants.FILE_TEMPLATE_NAME
                        + Constants.DOT + format;
                ClassPathResource resource = new ClassPathResource(templatePath);
                if (!resource.exists()) {
                    logger.warn("Template not found for seed task " + fileId + ": " + templatePath);
                    continue;
                }
                File target = new File(dir, fileName);
                try (InputStream in = resource.getInputStream();
                     OutputStream out = new FileOutputStream(target)) {
                    out.write(in.readAllBytes());
                }
                logger.info("Created seed file: " + target.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Failed to create seed file for " + fileId, e);
            }
        }
    }
}
