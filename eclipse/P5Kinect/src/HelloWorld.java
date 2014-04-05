import java.util.ArrayList;
import processing.core.*;
import p5Kinect.P5Kinect;
import p5Kinect.SkeletonData;

public class HelloWorld extends PApplet {

	private static final long serialVersionUID = 1L;
	private P5Kinect kinect;
	private PFont font;
	private ArrayList<SkeletonData> bodies;
	private PImage img;

	public void setup() {
		size(640, 480);
		background(0);
		kinect = new P5Kinect(this);
		bodies = new ArrayList<SkeletonData>();
		smooth();
		font = loadFont("LucidaSans-18.vlw");
		textFont(font, 18);
		textAlign(CENTER);
		img = loadImage("background.jpg");
	}

	public void draw() {
		background(0);
		image(kinect.GetImage(), 320, 0, 320, 240);
		image(kinect.GetDepth(), 320, 240, 320, 240);
		image(img, 0, 240, 320, 240);
		image(kinect.GetMask(), 0, 240, 320, 240);
		for (int i = 0; i < bodies.size(); i++) {
			drawSkeleton(bodies.get(i));
			drawPosition(bodies.get(i));
		}
	}

	public void mousePressed() {
		println(frameRate);
		saveFrame();
	}

	public void drawPosition(SkeletonData _s) {
		noStroke();
		fill(0, 100, 255);
		String s1 = str(_s.dwTrackingID);
		text(s1, _s.position.x * width / 2, _s.position.y * height / 2);
	}

	public void drawSkeleton(SkeletonData _s) {
		// Body
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_HEAD,
				P5Kinect.NUI_SKELETON_POSITION_SHOULDER_CENTER);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_SHOULDER_CENTER,
				P5Kinect.NUI_SKELETON_POSITION_SHOULDER_LEFT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_SHOULDER_CENTER,
				P5Kinect.NUI_SKELETON_POSITION_SHOULDER_RIGHT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_SHOULDER_CENTER,
				P5Kinect.NUI_SKELETON_POSITION_SPINE);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_SHOULDER_LEFT,
				P5Kinect.NUI_SKELETON_POSITION_SPINE);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_SHOULDER_RIGHT,
				P5Kinect.NUI_SKELETON_POSITION_SPINE);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_SPINE,
				P5Kinect.NUI_SKELETON_POSITION_HIP_CENTER);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_HIP_CENTER,
				P5Kinect.NUI_SKELETON_POSITION_HIP_LEFT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_HIP_CENTER,
				P5Kinect.NUI_SKELETON_POSITION_HIP_RIGHT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_HIP_LEFT,
				P5Kinect.NUI_SKELETON_POSITION_HIP_RIGHT);

		// Left Arm
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_SHOULDER_LEFT,
				P5Kinect.NUI_SKELETON_POSITION_ELBOW_LEFT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_ELBOW_LEFT,
				P5Kinect.NUI_SKELETON_POSITION_WRIST_LEFT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_WRIST_LEFT,
				P5Kinect.NUI_SKELETON_POSITION_HAND_LEFT);

		// Right Arm
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_SHOULDER_RIGHT,
				P5Kinect.NUI_SKELETON_POSITION_ELBOW_RIGHT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_ELBOW_RIGHT,
				P5Kinect.NUI_SKELETON_POSITION_WRIST_RIGHT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_WRIST_RIGHT,
				P5Kinect.NUI_SKELETON_POSITION_HAND_RIGHT);

		// Left Leg
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_HIP_LEFT,
				P5Kinect.NUI_SKELETON_POSITION_KNEE_LEFT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_KNEE_LEFT,
				P5Kinect.NUI_SKELETON_POSITION_ANKLE_LEFT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_ANKLE_LEFT,
				P5Kinect.NUI_SKELETON_POSITION_FOOT_LEFT);

		// Right Leg
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_HIP_RIGHT,
				P5Kinect.NUI_SKELETON_POSITION_KNEE_RIGHT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_KNEE_RIGHT,
				P5Kinect.NUI_SKELETON_POSITION_ANKLE_RIGHT);
		DrawBone(_s, P5Kinect.NUI_SKELETON_POSITION_ANKLE_RIGHT,
				P5Kinect.NUI_SKELETON_POSITION_FOOT_RIGHT);
	}

	public void DrawBone(SkeletonData _s, int _j1, int _j2) {
		noFill();
		stroke(255, 255, 0);
		if (_s.skeletonPositionTrackingState[_j1] != P5Kinect.NUI_SKELETON_POSITION_NOT_TRACKED
				&& _s.skeletonPositionTrackingState[_j2] != P5Kinect.NUI_SKELETON_POSITION_NOT_TRACKED) {
			line(_s.skeletonPositions[_j1].x * width / 2,
					_s.skeletonPositions[_j1].y * height / 2,
					_s.skeletonPositions[_j2].x * width / 2,
					_s.skeletonPositions[_j2].y * height / 2);
		}
	}

	public void appearEvent(SkeletonData _s) {
		if (_s.trackingState == P5Kinect.NUI_SKELETON_NOT_TRACKED) {
			return;
		}
		synchronized (bodies) {
			bodies.add(_s);
		}
	}

	public void disappearEvent(SkeletonData _s) {
		synchronized (bodies) {
			for (int i = bodies.size() - 1; i >= 0; i--) {
				if (_s.dwTrackingID == bodies.get(i).dwTrackingID) {
					bodies.remove(i);
				}
			}
		}
	}

	public void moveEvent(SkeletonData _b, SkeletonData _a) {
		if (_a.trackingState == P5Kinect.NUI_SKELETON_NOT_TRACKED) {
			return;
		}
		synchronized (bodies) {
			for (int i = bodies.size() - 1; i >= 0; i--) {
				if (_b.dwTrackingID == bodies.get(i).dwTrackingID) {
					bodies.get(i).copy(_a);
					break;
				}
			}
		}
	}

}
