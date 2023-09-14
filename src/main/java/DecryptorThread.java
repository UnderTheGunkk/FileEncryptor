import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

public class DecryptorThread extends Thread {
    private GUIForm form;
    private File file;
    private String password;

    public DecryptorThread(GUIForm form) {
        this.form = form;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        onStart();
        try {
            String outPath = getOutputPath();
            ZipFile zipFile = new ZipFile(file);
            zipFile.setPassword(password);
            zipFile.extractAll(outPath);
            onFinish();
        } catch (ZipException zEx) {
            if (zEx.getMessage().contains("Wrong Password")) {
                form.showWarning("Пароль введен неверно!");
            }
        } catch (Exception e) {
            form.showWarning(e.getMessage());
        }
        form.setButtonsEnabled(true);
    }

    private void onStart() {
        form.setButtonsEnabled(false);
    }

    private void onFinish() {
        form.setButtonsEnabled(true);
        form.showFinished();
    }

    private String getOutputPath() {
        String path = file.getAbsolutePath().replaceAll("\\.enc$", "");
        for (int i = 1; ; i++) {
            String number = i > 1 ? String.valueOf(i) : "";
            String outPath = path + number;
            if (!new File(outPath).exists()) {
                return outPath;
            }

        }

    }
}
