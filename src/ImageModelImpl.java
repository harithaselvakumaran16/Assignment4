import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageModelImpl implements ImageModel{

  Map<String, List<Integer>[][]> fileReferences  =  new HashMap<String, List<Integer>[][]>();
  @Override
  public void loadImage(String filePath, String referenceName) throws IOException {
    ImageUtil img = new ImageUtil();
    List<Integer>[][] image = img.readPPM(filePath);
    fileReferences.put(referenceName, image);
    System.out.println(fileReferences.get(referenceName)[0][0]);
  }

  @Override
  public void saveImage(String filePath, String referenceName) throws IOException {
    ImageUtil img = new ImageUtil();
    List<Integer>[][] image = fileReferences.get(referenceName);
    img.createPPMFile("P3", image[0].length, image.length, 255, image, filePath);
  }

  @Override
  public void flipImage(String flipOption, String imageName, String newImageName) throws IOException {
    ImageUtil img =  new ImageUtil();
    List<Integer>[][] image = fileReferences.get(imageName);
    List<Integer>[][] newImage = img.flipping(flipOption, image[0].length, image.length, image);
    fileReferences.put(newImageName, newImage);
  }

  @Override
  public void greyscaleImage(String componentOption, String imageName, String newImageName) throws IOException {
    ImageUtil img = new ImageUtil();
    List<Integer>[][] image = fileReferences.get(imageName);
    List<Integer>[][] newImage = img.greyscale(componentOption, image[0].length, image.length, image);
    fileReferences.put(newImageName, newImage);
  }

  @Override
  public void brightenImage(int value, String imageName, String newImageName) throws IOException {
    ImageUtil img = new ImageUtil();
    List<Integer>[][] image = fileReferences.get(imageName);
    List<Integer>[][] newImage = img.brightenDarken(value, image[0].length, image.length, image);
    fileReferences.put(newImageName, newImage);
  }

  @Override
  public void combineRGBImage(String rComponent, String gComponent, String bComponent, String newImageName) throws IOException {
    ImageUtil img = new ImageUtil();
    List<Integer>[][] rImage = fileReferences.get(rComponent);
    List<Integer>[][] gImage = fileReferences.get(gComponent);
    List<Integer>[][] bImage = fileReferences.get(bComponent);
    List<Integer>[][] newImage = img.combineComponents(rImage, gImage, bImage, rImage[0].length, rImage.length);
    fileReferences.put(newImageName, newImage);
  }



}
