import java.io.FileOutputStream;
import java.io.IOException;
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
public class Image_Util {
    static int width;
    static int height;
    static List<Integer>[][] pixelOfImage;

    /**
     * Read an image file in the PPM format and print the colors.
     *
     * @param filename the path of the file.
     */
    public static void readPPM(String filename) throws IOException {
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

//        for(int i =0;i<height;i++) {
//            for(int j =0;j<width;j++) {
//                System.out.print(pixelOfImage[i][j]);
//            }
//        }

        //Calling Functions for checking their functionality

        //greyscale("Luma");
        //BrightenDarken( -50);
        //flipping("Horizontal");
        //flipping("Vertical");
        List<Integer>[][] b = greyscale("Blue");
        List<Integer>[][] r = greyscale("Red");
        List<Integer>[][] g = greyscale("Green");

        combineGreyscaleToRGB(r,g,b);
        for(int i =0;i<height;i++) {
            for(int j =0;j<width;j++) {
                System.out.print(pixelOfImage[i][j]);
            }
        }

        //visualizeRGB("Green");
        //rgbSplit(pixelOfImage);
    }

    public static List<Integer>[][] visualizeRGB(String option) throws IOException {
        List<Integer>[][] outputArrayRGB = new List[height][width];

        System.arraycopy(pixelOfImage,0,outputArrayRGB,0,height);

        for(int i = 0;i < height;i++) {
            for(int j = 0;j < width;j++) {
                if(option.equals("Red")) {
                    outputArrayRGB[i][j].set(1, 0);
                    outputArrayRGB[i][j].set(2,0);
                }
                else if (option.equals("Green")) {
                    outputArrayRGB[i][j].set(0, 0);
                    outputArrayRGB[i][j].set(2,0);
                }
                else if (option.equals("Blue")) {
                    outputArrayRGB[i][j].set(0, 0);
                    outputArrayRGB[i][j].set(1,0);
                }
            }
        }
        createPPMFile("P3",width,height,255,outputArrayRGB);
        return outputArrayRGB;
    }

    public static void rgbSplit(List<Integer>[][] pixelOfImage) throws IOException {
        List<Integer>[][] r = greyscale("Red");
        createPPMFile("P3",width,height,255,r);
        List<Integer>[][] g = greyscale("Green");
        createPPMFile("P3",width,height,255,g);
        List<Integer>[][] b = greyscale("Blue");
        createPPMFile("P3",width,height,255,b);
    }

    public static List<Integer>[][] greyscale(String option) throws IOException {
        List<Integer>[][] outputArray = new List[height][width];

        //System.arraycopy(pixelOfImage,0,outputArray,0,height);

        for(int i = 0;i < height;i++) {
            for(int j = 0;j < width;j++) {
                outputArray[i][j] = pixelOfImage[i][j];
            }
        }

        for(int i = 0;i < height;i++) {
            for(int j = 0;j < width;j++) {
                //int temp = outputArray[i][j].get(position);
                if(option.equals("Red")) {
                    int temp1 = outputArray[i][j].get(0);
                    outputArray[i][j].set(1, temp1);
                    outputArray[i][j].set(2,temp1);
                }
                else if (option.equals("Green")) {
                    int temp2 = outputArray[i][j].get(1);
                    outputArray[i][j].set(0, temp2);
                    outputArray[i][j].set(2,temp2);
                }
                else if (option.equals("Blue")) {
                    int temp3 = outputArray[i][j].get(2);
                    outputArray[i][j].set(0, temp3);
                    outputArray[i][j].set(1,temp3);
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
            }
        }
       // createPPMFile("P3",width,height,255,outputArray);
        return outputArray;
    }


    public static List<Integer>[][] flipping(String option) throws IOException {
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

        createPPMFile("P3",width,height,255,outputArrayFlip);
        return outputArrayFlip;
    }

    public static List<Integer>[][] BrightenDarken(int value) throws IOException {
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
        createPPMFile("P3", width, height, 255, brighterDarker);
        return brighterDarker;
    }

    public static List<Integer>[][] combineGreyscaleToRGB(List<Integer>[][] r,
                                                          List<Integer>[][] g,
                                                          List<Integer>[][] b) throws IOException {

        List<Integer>[][] combinedGrayscale = new List[height][width];
        System.arraycopy(g, 0, combinedGrayscale, 0, height);

//        for(int i = 0;i<height;i++) {
//            for(int j =0;j<width;j++) {
//                System.out.print(pixelOfImage[i][j]);
//            }
//        }
        System.out.printf("helo");
        for(int i =0;i<height;i++) {
            for(int j =0;j<width;j++) {
                int firstElement = r[i][j].get(0);
                combinedGrayscale[i][j].set(0, firstElement);

                int secondElement = g[i][j].get(1);
                combinedGrayscale[i][j].set(1, secondElement);

                int thirdElement = b[i][j].get(2);
                combinedGrayscale[i][j].set(2, thirdElement);
            }
        }
        createPPMFile("P3",width,height,255,combinedGrayscale);
        return combinedGrayscale;
    }

    public static void createPPMFile(String token, int width, int height, int maxValue, List<Integer>[][] image) throws IOException, IOException {
        StringBuilder ppmFormat = new StringBuilder();
        ppmFormat.append(token + '\n' + width+" "+height+'\n' + maxValue+'\n');
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
        FileOutputStream fos = new FileOutputStream("Koala-copy.ppm");
        fos.write(new String(ppmFormat).getBytes());

        fos.close();
    }

  //demo main
  public static void main(String []args) throws IOException {
      String filename;
      
      if (args.length>0) {
          filename = args[0];
      }
      else {
          filename = "src/Koala.ppm";
      }

      Image_Util.readPPM(filename);
  }
}