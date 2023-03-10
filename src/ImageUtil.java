import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;


/**
 * This class contains utility methods to read a PPM image from file and simply print its contents. Feel free to change this method 
 *  as required.
 */
public class ImageUtil {
    static int width;
    static int height;
    static List[][] pixelOfImage;

    /**
     * Read an image file in the PPM format and print the colors.
     *
     * @param filename the path of the file.
     */
    public static void readPPM(String filename) {
        Scanner sc;

        try {
            sc = new Scanner(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename + " not found!");
            return;
        }
        StringBuilder builder = new StringBuilder();
        //read the file line by line, and populate a string. This will throw away any comment lines
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            if (s.charAt(0) != '#') {
                builder.append(s + System.lineSeparator());
            }
        }

        //System.out.print(builder);

        //now set up the scanner to read from the string we just built
        sc = new Scanner(builder.toString());

        String token;

        token = sc.next();
        if (!token.equals("P3")) {
            System.out.println("Invalid PPM file: plain RAW file should begin with P3");
        }
        width = sc.nextInt();
        System.out.println("Width of image: " + width);
        height = sc.nextInt();
        System.out.println("Height of image: " + height);
        int maxValue = sc.nextInt();
        System.out.println("Maximum value of a color in this file (usually 255): " + maxValue);
        pixelOfImage = new List[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                List<Integer> rgbvalues = new ArrayList<Integer>();
                int r = sc.nextInt();
                rgbvalues.add(r);
                int g = sc.nextInt();
                rgbvalues.add(g);
                int b = sc.nextInt();
                rgbvalues.add(b);
                pixelOfImage[i][j] = rgbvalues;
            }
        }
        //visualizeComponent(0);
        visualizeVILComponents("Luma");
    }

    public static List<Integer>[][] visualizeComponent(int position) {
        List<Integer>[][] outputArray = new List[height][width];

        for(int i =0;i<height;i++) {
            for(int j=0;j<width;j++) {
                outputArray[i][j] = pixelOfImage[i][j];
            }
        }

        for(int i = 0;i < height;i++) {
            for(int j = 0;j < width;j++) {
                int temp = outputArray[i][j].get(position);
                if(position==0) {
                    outputArray[i][j].set(1, temp);
                    outputArray[i][j].set(2,temp);
                }
                else if (position==1) {
                    outputArray[i][j].set(0, temp);
                    outputArray[i][j].set(2,temp);
                }
                else if (position==2) {
                    outputArray[i][j].set(0, temp);
                    outputArray[i][j].set(1,temp);
                }
            }
        }

        for(int i =0;i<height;i++) {
            for(int j=0;j<width;j++) {
                System.out.print(outputArray[i][j]);
            }
        }
        return outputArray;
    }

    public static List<Double>[][] visualizeVILComponents(String option) {
        List<Double>[][] outputArrayVIL = new List[height][width];

        for(int i =0;i<height;i++) {
            for(int j=0;j<width;j++) {
                outputArrayVIL[i][j] = pixelOfImage[i][j];
            }
        }

        for(int i = 0;i<height;i++) {
            for(int j = 0;j<height;j++) {
                List<Integer> temp = pixelOfImage[i][j];
                if (option.equals("Value")) {
                    double max = Collections.max(temp);
                    outputArrayVIL[i][j].set(0, max);
                    outputArrayVIL[i][j].set(1, max);
                    outputArrayVIL[i][j].set(2, max);
                }
                else if (option.equals("Intensity")) {
                    double average = (temp.get(0) + temp.get(1) + temp.get(2))/3;
                    outputArrayVIL[i][j].set(0, average);
                    outputArrayVIL[i][j].set(1, average);
                    outputArrayVIL[i][j].set(2, average);
                }
                else if (option.equals("Luma")) {
                    double weightedSum = 0.2126 * temp.get(0) + 0.7152 * temp.get(1) + 0.0722 * temp.get(2);
                    outputArrayVIL[i][j].set(0, weightedSum);
                    outputArrayVIL[i][j].set(1, weightedSum);
                    outputArrayVIL[i][j].set(2, weightedSum);
                }
            }
        }
        for(int i =0;i<height;i++) {
            for(int j=0;j<width;j++) {
                System.out.print(outputArrayVIL[i][j]);
            }
        }
        return outputArrayVIL;
    }


  //demo main
  public static void main(String []args) {
      String filename;
      
      if (args.length>0) {
          filename = args[0];
      }
      else {
          filename = "src/Koala.ppm";
      }

      ImageUtil.readPPM(filename);
  }
}

