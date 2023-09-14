import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;

public class EncryptorThread extends Thread {
    private GUIForm form;
    private File file;
    private ZipParameters parameters;

    public EncryptorThread(GUIForm form) {
        this.form = form;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setPassword(String password) {
        parameters = ParametersContainer.getParameters();
        parameters.setPassword(password);
    }

    private void onStart() {
        form.setButtonsEnabled(false);
    }

    private void onFinish() {
        parameters.setPassword("");
        form.setButtonsEnabled(true);
        form.showFinished();
    }

    @Override
    public void run() {
        onStart();
        try {
            String archiveName = getArchiveName();
            ZipFile zipFile = new ZipFile(archiveName);
            if (file.isDirectory()) {
                zipFile.addFolder(file, parameters);
            }
        } catch (Exception e) {
            form.showWarning(e.getMessage());
            return;
        }
        onFinish();
    }

    private String getArchiveName() {
        for (int i = 1; ; i++) {
            String number = i > 1 ? String.valueOf(i) : "";
            String archiveName = file.getAbsolutePath() + number + ".enc";
            if (!new File(archiveName).exists()) {
                return archiveName;
            }
        }
    }
}
