import java.io.Serializable;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

public class Index implements Serializable{
    private String filePath;
    private int lineNum;

    public Index(String filePath, int lineNum) {
        this.filePath = filePath;
        this.lineNum = lineNum;
    }

    public String getFilePath() {
        return filePath;
    }


    public int getLineNum() {
        return lineNum;
        AbortPolicy
    }

}
