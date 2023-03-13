import java.io.IOException;

import javax.imageio.IIOException;

public interface ImageModel {
  void loadImage(String filePath, String referenceName) throws IOException;
  void saveImage(String filePath, String referenceName) throws IOException;
  void flipImage(String flipOption, String imageName, String newImageName) throws IOException;
  void greyscaleImage(String componentOption, String imageName, String newImageName) throws IOException;
  void brightenImage(int value, String imageName, String newImageName) throws IOException;
  void combineRGBImage(String rComponent, String gComponent, String bComponent, String newImageName) throws IOException;
}
