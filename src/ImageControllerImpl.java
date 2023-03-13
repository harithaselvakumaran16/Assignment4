import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.UnexpectedException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ImageControllerImpl implements ImageController {
  private ImageModel model;
  private InputStream in;

  private OutputStream out;
  public ImageControllerImpl(ImageModel model, InputStream in, OutputStream out) {
    this.model = model;
    this.in = in;
    this.out = out;
  }
  private String checkComponent(String component) {
    if(component.equals("red-component")) {
      return "Red";
    }
    else if(component.equals("blue-component")) {
      return "Blue";
    }
    else if(component.equals("green-component")) {
      return "Green";
    }
    else if(component.equals("value-component")) {
      return "Value";
    }
    else if(component.equals("intensity-component")) {
      return "Intensity";
    }
    else if(component.equals("luma-component")) {
      return "Luma";
    }
    else {
      return "Unrecognized Component";
    }
  }
  public String commandHandler(ImageModel model, String command) throws IOException {
    String[] commandParts = command.split(" ");
    switch(commandParts[0]) {
      case "load":
        System.out.println("1");
        model.loadImage(commandParts[1], commandParts[2]);
        break;
      case "save":
        model.saveImage(commandParts[1], commandParts[2]);
        System.out.println("2");
        break;
      case "vertical-flip":
        model.flipImage("Vertical", commandParts[1], commandParts[2]);
        break;
      case "horizontal-flip":
        model.flipImage("Horizontal", commandParts[1], commandParts[2]);
        break;
      case "greyscale":
        if(!checkComponent(commandParts[1]).equals("Unrecognized Component")) {
          model.greyscaleImage(checkComponent(commandParts[1]), commandParts[2], commandParts[3]);
        }
        else {
          return checkComponent(commandParts[1]);
        }
        break;
      case "rgb-split":
        model.greyscaleImage("Red", commandParts[1], commandParts[2]);
        model.greyscaleImage("Green", commandParts[1], commandParts[3]);
        model.greyscaleImage("Blue", commandParts[1], commandParts[4]);
        break;
      case "brighten":
        model.brightenImage(Integer.parseInt(commandParts[1]), commandParts[2], commandParts[3]);
        break;
      case "rgb-combine":
        model.combineRGBImage(commandParts[2], commandParts[3], commandParts[4], commandParts[1]);
        break;
      case "run-script":
        break;
      default:
        return "Incorrect Command";
    }
    return "";
  }

  @Override
  public void go() throws IOException {
    String command;
    PrintStream outStream = new PrintStream(this.out);
    Scanner scan = new Scanner(in);
    while(true) {
      command = scan.nextLine();
      //outStream.printf("%d", model.add(num1, num2));
      outStream.printf(commandHandler(model, command));
    }
  }


}
