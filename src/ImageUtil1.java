import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;


/**
 * This class contains utility methods to read a PPM image from file and simply print its contents. Feel free to change this method 
 *  as required.
 */
public class ImageUtil {

  public void createPPMFile(String token, int width, int height, int maxValue,
                            List<Integer>[][] image, String filePath) throws IOException {
    StringBuilder ppmFormat = new StringBuilder();
    ppmFormat.append(token + '\n' + "# Created by GIMP version 2.10.20 PNM plug-in" + '\n' + width+" "+height+'\n' + maxValue+'\n');
    //ppmFormat.append();
    //ppmFormat.append(maxValue+'\n');
    for (int i=0;i<height;i++) {
      for (int j=0;j<width;j++) {
        for (int k : image[i][j]) {
        ppmFormat.append(k);
        ppmFormat.append('\n');
        }
      }
      ppmFormat.append('\n');
    }
    FileOutputStream fos = new FileOutputStream(filePath);
    fos.write(new String(ppmFormat).getBytes());

    fos.close();
  }
  public List<Integer>[][] flipping(String option, int width, int height,
                                           List<Integer>[][] pixelOfImage) throws IOException {
    List<Integer>[][] outputArrayFlip = new List[height][width];
    System.arraycopy(pixelOfImage,0,outputArrayFlip,0,height);

    if(option.equals("Horizontal")) {
      for(int i =0;i<height;i++) {
        for (int j = 0; j < width/2; j++) {
          List<Integer> temp = outputArrayFlip[i][j];
          outputArrayFlip[i][j] = outputArrayFlip[i][width-j-1];
          outputArrayFlip[i][width-j-1] = temp;
        }
      }
    }

    if(option.equals("Vertical")) {
      for(int i =0;i<height/2;i++) {
        for (int j = 0; j < width; j++) {
          List<Integer> temp = outputArrayFlip[i][j];
          outputArrayFlip[i][j] = outputArrayFlip[height-i-1][j];
          outputArrayFlip[height-i-1][j] = temp;
        }
      }
    }

    //createPPMFile("P3",width,height,255,outputArrayFlip);
    return outputArrayFlip;
  }


  public List<Integer>[][] greyscale(String option, int width, int height,
                                            List<Integer>[][] pixelOfImage) throws IOException {
    List<Integer>[][] outputArray = new List[height][width];

    //System.arraycopy(pixelOfImage,0,outputArray,0,height);

    for(int i = 0;i < height;i++) {
      for(int j = 0;j < width;j++) {
        List<Integer> val = new ArrayList<>();
        //int temp = outputArray[i][j].get(position);
        if(option.equals("Red")) {
          int temp1 = pixelOfImage[i][j].get(0);
//          outputArray[i][j].set(1, temp1);
//          outputArray[i][j].set(2,temp1);
         val.add(temp1);
         val.add(temp1);
         val.add(temp1);
        }
        else if (option.equals("Green")) {
          int temp2 = pixelOfImage[i][j].get(1);
//          outputArray[i][j].set(0, temp2);
//          outputArray[i][j].set(2,temp2);
          val.add(temp2);
          val.add(temp2);
          val.add(temp2);
        }
        else if (option.equals("Blue")) {
          int temp3 = pixelOfImage[i][j].get(2);
//          outputArray[i][j].set(0, temp3);
//          outputArray[i][j].set(1,temp3);
          val.add(temp3);
          val.add(temp3);
          val.add(temp3);

        }
        else if (option.equals("Value")) {
          List<Integer> temp = pixelOfImage[i][j];
          int max = Collections.max(temp);
          outputArray[i][j].set(0, max);
          outputArray[i][j].set(1, max);
          outputArray[i][j].set(2, max);
        }
        else if (option.equals("Intensity")) {
          List<Integer> temp = pixelOfImage[i][j];
          int average = (temp.get(0) + temp.get(1) + temp.get(2))/3;
          outputArray[i][j].set(0, average);
          outputArray[i][j].set(1, average);
          outputArray[i][j].set(2, average);
        }
        else if (option.equals("Luma")) {
          List<Integer> temp = pixelOfImage[i][j];
          double weightedSum = 0.2126 * temp.get(0) + 0.7152 * temp.get(1) + 0.0722 * temp.get(2);
          outputArray[i][j].set(0, (int) weightedSum);
          outputArray[i][j].set(1, (int) weightedSum);
          outputArray[i][j].set(2, (int) weightedSum);
        }
        outputArray[i][j] = val;
      }
    }
    createPPMFile("P3",width,height,255,outputArray);
    return outputArray;
  }

  public List<Integer>[][] brightenDarken(int value, int width, int height,
                                                 List<Integer>[][] pixelOfImage) throws IOException {
    List<Integer>[][] brighterDarker = new List[height][width];

    System.arraycopy(pixelOfImage,0,brighterDarker,0,height);

    for(int i =0;i<height;i++) {
      for(int j =0;j<width;j++) {
        if(value > 0) {
          int firstElement = Math.min(brighterDarker[i][j].get(0) + value, 255);
          brighterDarker[i][j].set(0, firstElement);
          int secondElement = Math.min(brighterDarker[i][j].get(1) + value, 255);
          brighterDarker[i][j].set(1, secondElement);
          int thirdElement = Math.min(brighterDarker[i][j].get(2) + value, 255);
          brighterDarker[i][j].set(2, thirdElement);
        }
        else if (value < 0) {
          int firstElement = Math.max(brighterDarker[i][j].get(0) + value, 0);
          brighterDarker[i][j].set(0, firstElement);
          int secondElement = Math.max(brighterDarker[i][j].get(1) + value, 0);
          brighterDarker[i][j].set(1, secondElement);
          int thirdElement = Math.max(brighterDarker[i][j].get(2) + value, 0);
          brighterDarker[i][j].set(2, thirdElement);
        }
      }
    }
    //createPPMFile("P3", width, height, 255, brighterDarker);
    return brighterDarker;
  }
  public List<Integer>[][] combineComponents(List<Integer>[][] r,
                                             List<Integer>[][] g,
                                             List<Integer>[][] b, int width, int height) throws IOException {
    List<Integer>[][] combinedImage = new List[height][width];

    // System.arraycopy(r,0,combinedImage,0,height);
    for(int i =0;i<height;i++) {
      for (int j = 0; j < width; j++) {
        List<Integer> rgbValues = new ArrayList<>();
        int firstElement = r[i][j].get(0);
        //combinedImage[i][j].set(0, firstElement);
        rgbValues.add(firstElement);
        int secondElement = g[i][j].get(1);
        //combinedImage[i][j].set(1, secondElement);
        rgbValues.add(secondElement);
        int thirdElement = b[i][j].get(2);
        //combinedImage[i][j].set(2, thirdElement);
        rgbValues.add(thirdElement);
        combinedImage[i][j] = rgbValues;
      }
    }
    return combinedImage;
  }

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param filename the path of the file. 
   */
  public List<Integer>[][] readPPM(String filename) throws IOException {
    Scanner sc;
    
    try {
        sc = new Scanner(new FileInputStream(filename));
    }
    catch (FileNotFoundException e) {
        System.out.println("File "+filename+ " not found!");
        return null;
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
        String s = sc.nextLine();
        if (s.charAt(0)!='#') {
            builder.append(s+System.lineSeparator());
        }
    }
    
    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token; 

    token = sc.next();
    if (!token.equals("P3")) {
        System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    System.out.println("Width of image: "+width);
    int height = sc.nextInt();
    System.out.println("Height of image: "+height);
    int maxValue = sc.nextInt();
    System.out.println("Maximum value of a color in this file (usually 255): "+maxValue);
 //   List<List<Integer>> image = new ArrayList<List<Integer>>();
    List<Integer>[][] image = new List[height][width];
    for (int i=0;i<height;i++) {
        for (int j=0;j<width;j++) {
            List<Integer> rgb = new LinkedList<>();
            int r = sc.nextInt();
            int g = sc.nextInt();
            int b = sc.nextInt();
            rgb.add(r);
            rgb.add(g);
            rgb.add(b);
            //System.out.println("Color of pixel ("+j+","+i+"): "+ r+","+g+","+b);
            image[i][j] = rgb;
        }
    }
    //System.out.println(image[0].length);
   // ImageUtil.createPPMFile(token, width, height, maxValue, image);

    return image;
  }

  //demo main
  public static void main(String []args) throws IOException {
      String filename;
      Image_Util img = new Image_Util();
      
      if (args.length>0) {
          filename = args[0];
      }
      else {
          filename = "src/Koala.ppm";
      }

      //img.readPPM(filename);
      ImageModel model =  new ImageModelImpl();
      ImageController controller = new ImageControllerImpl(model, System.in, System.out);
      controller.go();
  }
}

