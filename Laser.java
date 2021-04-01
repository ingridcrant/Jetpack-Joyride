import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class Laser extends Rectangle {
    private BufferedImage laser = JetpackJoyridePanel.loadBuffImg("laser.png");
    private BufferedImage firingLaser = JetpackJoyridePanel.loadBuffImg("laserfiring.png");
    private static final int LASERSPEED = 5, LOADINGELLIPSESPEED = 3;
    private static final int FRAMESBEFOREPOSITION = 20, FRAMESBEFORECOOLDOWN = 40, FRAMESBEFOREOFF = 18;
    private int frameNum;

    public static final int LEFT = 0, RIGHT = 1;

    public static final int LASERBEAMGAP = 30;

    private boolean isMoving, atPosition, firing, cooling, off;

    private int ellipseRadiusX, ellipseRadiusY;
    private Ellipse2D.Double loadingEllipse;
    private Point2D.Double loadingLineEndPoint;

    private Point center;
    private int width, height;
    private int dir;

    public Laser(int ddir, int yy) {
        super();
        dir = ddir;

        width = laser.getWidth();
        height = laser.getHeight();

        isMoving = true; atPosition = false; firing = false; cooling = false; off = false;  // the laser is moving

        center = new Point();

        if(dir == LEFT) {
            center.x = JetpackJoyridePanel.WIDTH + 50 + laser.getWidth(null)/2;
            laser = JetpackJoyridePanel.flipImage(laser);
            firingLaser = JetpackJoyridePanel.flipImage(firingLaser);
        } else {
            center.x = -50 - laser.getWidth(null)/2;
        }
        center.y = yy + laser.getHeight(null)/2;

        frameNum = 0;
        ellipseRadiusX = 100;
        ellipseRadiusY = 80;
        loadingEllipse = new Ellipse2D.Double(center.x - ellipseRadiusX, center.y - ellipseRadiusY, 2*ellipseRadiusX, 2*ellipseRadiusY);
        loadingLineEndPoint = new Point2D.Double();

        setBounds(center.x - width/2, center.y - height/2, width, height);
    }
    public Point2D getFiringEndPoint() {
        double x;
        if(dir == RIGHT) {
            x = center.x - laser.getWidth(null)/2 + firingLaser.getWidth();
        } else {
            x = center.x + laser.getWidth(null)/2 - firingLaser.getWidth();
        }
        double y = center.y - firingLaser.getHeight(null)/2 + LASERBEAMGAP;

        return new Point2D.Double(x,y);
    }
    public Point2D getLoadingLineEndPoint() {
        return loadingLineEndPoint;
    }

    // Getter methods:
    public boolean isAtPosition() {
        return atPosition;
    }
    public boolean isFiring() {
        return firing;
    }
    public boolean isOff() {
        return off;
    }

    public void move() {
        if(dir == RIGHT && isMoving && frameNum == FRAMESBEFOREPOSITION) {
            isMoving = false; atPosition = true;                                    // the laser is at position
            SoundPlayer.playSoundEffect(SoundPlayer.laserLoading, 0);
            frameNum = 0;
        }
        else if (dir == LEFT && isMoving && frameNum == FRAMESBEFOREPOSITION) {
            isMoving = false; atPosition = true;                                    // the laser is at position
            frameNum = 0;
        }
        else if(firing && frameNum == FRAMESBEFORECOOLDOWN) {
            firing = false; cooling = true;                                         // the laser is cooling
            JetpackJoyridePanel.resetlaserBeamRect();
            frameNum = 0;
        }
        else if(cooling && frameNum == FRAMESBEFOREOFF) {
            cooling = false;
            off = true;                                                             // the laser is off
            frameNum = 0;
        }

        if(isMoving) {
            if(dir == RIGHT) {
                center.x += LASERSPEED;
            } else {
                center.x -= LASERSPEED;
            }
            setLocation(center.x - laser.getWidth(null)/2, center.y - laser.getHeight(null)/2);
            frameNum++;
        }
        else if(atPosition) {
            if(loadingEllipse.getWidth() <= getWidth() || loadingEllipse.getHeight() <= getHeight()) {
                atPosition = false;
                firing = true;
                SoundPlayer.playSoundEffect(SoundPlayer.laserFiring, 3);
                return;
            }
            loadingEllipse.setFrame(center.x - ellipseRadiusX, center.y - ellipseRadiusY, 2*ellipseRadiusX, 2*ellipseRadiusY);
            if(dir == RIGHT) {
                loadingLineEndPoint.x = center.x + ellipseRadiusX;
            }
            else {
                loadingLineEndPoint.x = center.x - ellipseRadiusX;
            }
            loadingLineEndPoint.y = center.y;
            ellipseRadiusX -= LOADINGELLIPSESPEED;
            ellipseRadiusY -= LOADINGELLIPSESPEED;
        }
        else if(firing) {
            if(dir == RIGHT) {
                setBounds(center.x - laser.getWidth(null)/2 , center.y - firingLaser.getHeight(null)/2, firingLaser.getWidth(null), firingLaser.getHeight(null));
            }
            else {
                setBounds(center.x + laser.getWidth(null)/2 - firingLaser.getWidth(null), center.y - firingLaser.getHeight(null)/2, firingLaser.getWidth(null), firingLaser.getHeight(null));
            }
            frameNum++;
        }
        else if(cooling) {
            if(dir == RIGHT) {
                center.x -= LASERSPEED;
            } else {
                center.x += LASERSPEED;
            }
            setLocation(center.x - laser.getWidth(null)/2, center.y - laser.getHeight(null)/2);
            frameNum++;
        }
    }
    public void draw(Graphics g) {
        if(isMoving) {
            g.drawImage(laser, center.x - laser.getWidth(null)/2, center.y - laser.getHeight(null)/2, null);
        }
        else if(atPosition) {
            g.drawImage(laser, center.x - laser.getWidth(null)/2 , center.y - laser.getHeight(null)/2, null);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            g2d.draw(loadingEllipse);

        }
        else if(firing) {
            if(dir == RIGHT) {
                g.drawImage(firingLaser, center.x - laser.getWidth(null)/2 , center.y - firingLaser.getHeight(null)/2, null);
            }
            else if(dir == LEFT) {
                g.drawImage(firingLaser, center.x + laser.getWidth(null)/2 - firingLaser.getWidth(null), center.y - firingLaser.getHeight(null)/2, null);
            }
        }
        else if(cooling) {
            g.drawImage(laser, center.x - laser.getWidth(null)/2, center.y - laser.getHeight(null)/2, null);
        }
    }
}
