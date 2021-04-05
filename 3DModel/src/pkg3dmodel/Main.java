package pkg3dmodel;

/* 
* The code below is written by understanding the concept of Java 3D from various soruces like 
* The Java 3D Site at Sun - http://www.sun.com/desktop/java3d
* The Java 3D Repository - http://java3d.sdsc.edu 
* The Java 3D specification - http://www.javasoft.com/products/java-media/3D/
* The Java 3D API Specification book by Henry Sowizral, Kevin Rushforth, Michael Deering and published by Addison-Wesley
* The 3D model of the Strawberry is downloaded from Google Poly and Blender was used to edit the model.
* @author sadya 
*/

import java.applet.*;
import java.awt.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.behaviors.keyboard.*;

import com.sun.j3d.loaders.Scene;

import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;


import java.io.*;
import javafx.scene.Group;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.sun.j3d.utils.geometry.Text2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import static pkg3dmodel.Experiment_window.Participant_id;
import static pkg3dmodel.Experiment_window.technique_name;
public class Main extends Applet implements MouseListener, MouseMotionListener {

    
        Scene s = null;
	public SimpleUniverse universe = null;
	public Canvas3D canvas = null;
        //create a TransformGroup that we will be rotating
	public TransformGroup viewtrans = null;

	public TransformGroup tg = null;
	public Transform3D t3d = null;
        public TransformGroup tg2 = null;
	public Transform3D t3d2 = null;
	public Transform3D t3dstep = new Transform3D();
	public Matrix4d matrix = new Matrix4d();
        public static Matrix3d initial_rot_matrix = new Matrix3d();
        public static Point3d initial_xyz_point = new Point3d();
        
        public static Matrix3d final_rot_matrix = new Matrix3d();
        public AxisAngle4f finalaxis = new AxisAngle4f();
        public AxisAngle4f initialaxis = new AxisAngle4f();
        public static float[] initarr = new float[16]; 
        public static float[] finalarr = new float[16]; 
       
        public static float[] finalarr_test = new float[16]; 
        public static Matrix3d final_rot_matrix_test = new Matrix3d();
        public static Point3d final_xyz_point = new Point3d(); 
        public static ArrayList<Double> final_angles =null;
         
        public static Matrix3d template_rot_matrix = new Matrix3d();
        public Transform3D templatet3d = null;
        public TransformGroup templatetg = null;
	public Transform3D templatet3dstep = new Transform3D();
        public static float[] template_initarr = new float[16]; 
        public static Point3d template_xyz_point = new Point3d();
        public static ArrayList<Double> template_angles =null;
        
        public Text2D text2d;
	public String ss;
        public static JButton start_btn1 = new JButton ("Start");
        public static JButton btn2 = new JButton ("Stop");
       // public static final JRadioButton save_btn = new JRadioButton ("Save");
        public static JCheckBox save_btn = new JCheckBox ("Save");
      
        public static String final_orientation = "";
        public static String initial_orientation = "";
        public static String template_initial_orientation = "";
        public static HashMap<String,ArrayList> dict = new HashMap<String,ArrayList>();
        

        static String task_blockname = "3D Task";
        static String individual_taskname = "3D Model Rotation";
        public static ArrayList<String> user_data = new ArrayList<String>();
        
        static Frame final_fr =null;
       // public BranchGroup scene=null;
	public Main() {
		setLayout(new BorderLayout());
                
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
                
             
		canvas = new Canvas3D(config);
                 
		add("Center", canvas);
                start_btn1.setBackground(Color.RED);
                start_btn1.setPreferredSize(new Dimension(65,65));
                add("West", start_btn1);
                btn2.setBackground(Color.RED);
                btn2.setPreferredSize(new Dimension(65,65));
                add("East", btn2);
       
                save_btn.setBackground(Color.RED);
                save_btn.setPreferredSize(new Dimension(50,50));
                save_btn.setSelected(false);
                add("South", save_btn);
                
		universe = new SimpleUniverse(canvas);


                BranchGroup scene = createSceneGraph();
              
		universe.getViewingPlatform().setNominalViewingTransform();

		universe.getViewer().getView().setBackClipDistance(100.0);
                canvas.addMouseMotionListener(this);
                canvas.addMouseListener(this);
	//	canvas.addKeyListener(this);

		universe.addBranchGraph(scene);
	}

	public BranchGroup createSceneGraph() {
		BranchGroup objRoot = new BranchGroup();

		BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);

		viewtrans = universe.getViewingPlatform().getViewPlatformTransform();

                MouseRotate mouseRot = new MouseRotate(viewtrans);
                
               // viewtrans.addChild(mouseRot);
               // MouseBehavior mb = new MouseBehavior(viewtrans);
                
                
	       //KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewtrans);
	       //keyNavBeh.setSchedulingBounds(bounds);
		PlatformGeometry platformGeom = new PlatformGeometry();
                platformGeom.addChild(mouseRot);
	        //platformGeom.addChild(keyNavBeh);
		universe.getViewingPlatform().setPlatformGeometry(platformGeom);

		objRoot.addChild(createBird());
                objRoot.addChild(createBird2());
                
                objRoot.addChild(createText2D());
		return objRoot;
	}

	public BranchGroup createBird() {
       
		BranchGroup objRoot = new BranchGroup();
		tg = new TransformGroup();
		t3d = new Transform3D();
                
                //The WRITE capability must be set so that the behavior can
                //change the Transform3D in the TransformGroup
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                Random r = new Random();
                float random = 2.0f + r.nextFloat() * (2.0f - 10.0f);
		t3d.setTranslation(new Vector3d(0.0, -0.0, -20.0));
                // t3d.setRotation(new AxisAngle4f(-6.750813779114223f,-0.00594440529437913f,0.03955007273304152f,0.0f));
		t3d.setRotation(new AxisAngle4f(random, 2.0f, random, -1.2f)); //A four-element axis angle represented by single-precision floating point x,y,z,angle components. 
                t3d.get(initarr);                                                               //An axis angle is a rotation of angle (radians) about the vector (x,y,z)
                t3d.get(initial_rot_matrix); // initial rotation matrix - 9elements
                initial_xyz_point = getEulerRotation(initial_rot_matrix);
                
                String s_random = String.valueOf(random);
                initial_orientation = s_random+", 2.0f ,"+s_random+", -1.2f";
	        t3d.setScale(0.04);
	        // t3d.setScale(0.04);

		tg.setTransform(t3d);

		ObjectFile loader = new ObjectFile();
		//Scene s = null;

		File file = new java.io.File("model/Strawberry.obj");

		try {
			s = loader.load(file.toURI().toURL());
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}

		tg.addChild(s.getSceneGroup());
                tg.addChild(createLight());
                // templatetg.addChild(s.getSceneGroup());
                /// objRoot.addChild(templatetg);
		objRoot.addChild(tg);
		//objRoot.addChild(createLight());
                
		objRoot.compile();

		return objRoot;

	}
        public BranchGroup createBird2() {
       
		BranchGroup objRoot = new BranchGroup();
//		tg = new TransformGroup();
//		t3d = new Transform3D();
                
                templatetg = new TransformGroup();
		templatet3d = new Transform3D();
                templatetg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                Random r = new Random();
                float random2 = 5.0f + r.nextFloat() * (2.0f - 20.0f);
               // templatet3d.set(template_matrix);
                templatet3d.setTranslation(new Vector3d(0.0, -0.0, -20.0));
                templatet3d.setRotation(new AxisAngle4f(random2, 2.0f, random2, -1.2f));
                templatet3d.get(template_initarr);    // places the values of this transform into the single precision array of length 16.
                templatet3d.get(template_rot_matrix); // Places the normalized rotational component of this transform into the 3x3 matrix argument.
                template_xyz_point=getEulerRotation(template_rot_matrix); // point3d x y z coordinates
                template_angles = calculateeulerangles(template_rot_matrix);
                String s_random2 = String.valueOf(random2);
                template_initial_orientation = s_random2+", 2.0f ,"+s_random2+", -1.2f";
                templatet3d.setScale(0.04);
		templatetg.setTransform(templatet3d);
                
                //The WRITE capability must be set so that the behavior can
                //change the Transform3D in the TransformGroup

		ObjectFile loader = new ObjectFile();
		//Scene s = null;

		File file = new java.io.File("model/Strawberry_grey.obj");

		try {
			s = loader.load(file.toURI().toURL());
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}

		//tg.addChild(s.getSceneGroup());
               // s.getSceneGroup().getChild(0).
                templatetg.addChild(s.getSceneGroup());
                templatetg.addChild(createLight2());
                objRoot.addChild(templatetg);
		//objRoot.addChild(tg);
		//objRoot.addChild(createLight2());
                
		objRoot.compile();

		return objRoot;

	}
        public BranchGroup createText2D() {

            BranchGroup objRoot = new BranchGroup();
            tg2 = new TransformGroup();
            t3d2 = new Transform3D();

            t3d2.setTranslation(new Vector3d(0.0, -0.5, 0.0));
            t3d2.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
            t3d2.setScale(1.0);
            tg2.setTransform(t3d2);
    
          
//            ss = this.ShuffleList();
//            text2d = new Text2D("      " + ss, new Color3f(0.9f, 1.0f, 1.0f), "Helvetica", 14, Font.ITALIC);

            tg2.addChild(text2d);
            objRoot.addChild(tg2);

            objRoot.compile();

            return objRoot;

	}

      
	public Light createLight() {
		DirectionalLight light1 = new DirectionalLight(true, new Color3f(1.0f, 1.0f, 1.0f),
				new Vector3f(-0.3f, 0.2f, -1.0f));

		light1.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

		return light1;
	}
        public Light createLight2() {
            
//                AmbientLight lightA = new AmbientLight();
//                lightA.setInfluencingBounds(new BoundingSphere());
		DirectionalLight light2 = new DirectionalLight(true, new Color3f(3.0f, 3.0f, 3.0f),
				new Vector3f(-0.3f, 0.2f, -1.0f));

		light2.setInfluencingBounds(new BoundingSphere(new Point3d(), 15000.0));

		return light2;
	}

         public static String ShuffleList() {
           
           // create an array list object       
//           ArrayList myList = new ArrayList();
//
//           // populate the list
//           myList.add("Position it upwards");
//           myList.add("Position it downwards");
//           myList.add("Position it towards your left"); 
//           myList.add("Position it towards your right");
//           myList.add("Position it diagonally in any direction"); 
//
//           System.out.println("Collection after: "+ myList);
         
            int sample = 600;

//            Timer T = new Timer(sample, new ActionListener(){
//                String r = null;
//            @Override
//            public void actionPerformed(ActionEvent e) {
           // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
              // create an array list object       
           ArrayList myList = new ArrayList();

           // populate the list
           myList.add("Position it upwards");
           myList.add("Position it downwards");
           myList.add("Position it towards your left"); 
           myList.add("Position it towards your right");
           myList.add("Position it diagonally in any direction"); 

         //  System.out.println("Collection after: "+ myList);
           Collections.shuffle(myList);
           //System.out.print("fkjeoijf");
         //  System.out.println("Collection after shuffle: "+ myList);
         
     //   }
    //       });
           // shuffle the list
//           Collections.shuffle(myList);
 //          T.start();
//           System.out.println("Collection after shuffle: "+ myList);
           return (String) myList.get(0);
       
        } 
         
        public static Point3d getEulerRotation(Matrix3d m1) {
            Point3d Rotation = new Point3d();

            // extract the rotation angles from the upper 3x3 rotation
            // component of the 4x4 transformation matrix
            Rotation.y = -java.lang.Math.asin(m1.getElement(2, 0));
            double c = java.lang.Math.cos(Rotation.y);   
            double tRx, tRy, tRz;
              
            if (java.lang.Math.abs(Rotation.y) > 0.00001) {
              tRx = m1.getElement(2, 2) / c;
              tRy = -m1.getElement(2, 1) / c;

              Rotation.x = java.lang.Math.atan2(tRy, tRx);
              
              tRx = m1.getElement(0, 0) / c;
              tRy = -m1.getElement(1, 0) / c;

              Rotation.z = java.lang.Math.atan2(tRy, tRx);
            } else {
              Rotation.x = 0.0;

              tRx = m1.getElement(1, 1);
              tRy = m1.getElement(0, 1);

              Rotation.z = java.lang.Math.atan2(tRy, tRx);
            }

            Rotation.x = -Rotation.x;
            Rotation.z = -Rotation.z;

            // now try to ensure that the values are positive by adding 2PI if
            // necessary...
            if (Rotation.x < 0.0)
              Rotation.x += 2 * java.lang.Math.PI;

            if (Rotation.y < 0.0)
              Rotation.y += 2 * java.lang.Math.PI;

            if (Rotation.z < 0.0)
              Rotation.z += 2 * java.lang.Math.PI;

            return Rotation;
  }
        public ArrayList<Double> calculateeulerangles(Matrix3d m){
            ArrayList<Double> a = new ArrayList<Double>();
            double roll=Math.toDegrees(java.lang.Math.atan2(m.getElement(2, 1),m.getElement(2, 2)));  //x-axis 
            double pitch=Math.toDegrees(java.lang.Math.atan2(-m.getElement(2, 0),Math.sqrt(Math.pow(m.getElement(2, 1),2)
                    +Math.pow(m.getElement(2, 2),2))));       // y axis
            double yaw=Math.toDegrees(java.lang.Math.atan2(m.getElement(1, 0),(m.getElement(0, 0)))); // z - axis
            
           
            a.add(roll);
            a.add(pitch);
            a.add(yaw);
            return a;
        }
        
        public static void mainmethod() throws InterruptedException{
         
           ////////////////////////////////////////////////////////////////////
            
           user_data.add(Participant_id);
           user_data.add(technique_name);
           user_data.add(task_blockname);
           user_data.add(individual_taskname);
           
           
            ArrayList<Date> time = new ArrayList<Date>();
//            Date start_d = new Date();
//            time.add(start_d);
            ArrayList<String> list_of_init_orientation = new ArrayList<String>(); 
            ArrayList<String> template_list_of_init_orientation = new ArrayList<String>();
        
            ArrayList<Float> initial_matrix_arraylist =  new ArrayList<>();
            ArrayList<Float> template_matrix_arraylist =  new ArrayList<Float>();

            ArrayList<Float> final_matrix_arraylist_test =  new ArrayList<Float>();

            ArrayList<Point3d> xyz_points =  new ArrayList<Point3d>();
            ArrayList<ArrayList<Double>> angles = new ArrayList<>();
            
            float arr[] = new float[40]; 
            double marr[] = new double[40];
            ArrayList<Double> template_matrix_values = new ArrayList<Double>();
            ArrayList<Double> initial_matrix_values = new ArrayList<Double>();
        
            ArrayList<Double> final_matrix_values_test = new ArrayList<Double>();
           
           
            ArrayList<Frame> list_of_frames = new ArrayList<>(); 
            ArrayList<timer3d> list_of_timers = new ArrayList<>(); 
            timer3d t ;
            
           double tmv =0;
           double imv =0;
           double fmv=0;
           int count = 0;
           //  System.out.print(ew.indicator + "main"); 
         //  double fmv_test=0;
           save_btn.setSelected(false); 
           // if(count>0){
    //           while(!save_btn.isSelected()){
            while(count!=3){
                //frame.setVisible(false);
                count++;
              //  Thread.currentThread().sleep(10000);
		Main applet = new Main();
               
		Frame frame = new MainFrame(applet, 800, 600);
                list_of_frames.add(frame);
                
       //         Thread.currentThread().sleep(15000);
                
                list_of_init_orientation.add(initial_orientation);
                template_list_of_init_orientation.add(template_initial_orientation);
                   for(int i=0;i<=2;i++)
                        {
                    for(int j=0;j<=2;j++)
                        {
                          tmv=applet.template_rot_matrix.getElement(i, j);
                          template_matrix_values.add(tmv);
                        }
                        }
                    for(int i=0;i<=2;i++)
                        {
                    for(int j=0;j<=2;j++)
                        {
                          imv=applet.initial_rot_matrix.getElement(i, j);
                          initial_matrix_values.add(imv);
                        }
                        }
      
  
                applet.t3d.get(arr);
                 
             
            //    Lit al = Arrays.asList(arr);
                      
                for (Float d: initarr){
                 //   System.out.println(initarr.length + "initarr");
                    initial_matrix_arraylist.add(d);
                }
                
                   
                for (Float tm: template_initarr){
                    //System.out.println(template_initarr.length + "template_initarr");
                    template_matrix_arraylist.add(tm);
                }
                xyz_points.add(initial_xyz_point);
                xyz_points.add(template_xyz_point);
                angles.add(template_angles);
                
                start_btn1.addActionListener(new ActionListener(){
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        timer3d t = new timer3d();
                        t.runTimer();  
                        t.setLocation(900, 250);

                        t.setVisible(true);
                        list_of_timers.add(t);
                        if(list_of_timers.size()==3){
                            list_of_timers.get(0).setVisible(false);
                              list_of_timers.get(1).setVisible(false);
                        }
                        if(list_of_timers.size()==5){
                             System.out.println(list_of_timers.size());
                             list_of_timers.get(0).setVisible(false);
                             list_of_timers.get(1).setVisible(false);
                             list_of_timers.get(2).setVisible(false);
                             list_of_timers.get(3).setVisible(false);
                             list_of_timers.get(4).setVisible(false);
                        }
                        
                        Date start_d = new Date();
                        time.add(start_d);
                        System.out.println("Timer task started at " + new Date());


                    }
                    
                });
                
                
                
                
                save_btn.addActionListener(new ActionListener(){
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                     //   System.out.println("TO CHECK HOW MANY TIMES PRESSED");
                    Date end_d = new Date();
                    time.add(end_d);
                    
                    applet.t3d.get(finalarr_test);
                    applet.t3d.get(final_rot_matrix_test);
                    final_xyz_point = applet.getEulerRotation(final_rot_matrix_test); 
                    final_angles = applet.calculateeulerangles(final_rot_matrix_test);
                    xyz_points.add(final_xyz_point);
                    angles.add(final_angles);
                    
                    for (Float f_test: finalarr_test){
                     System.out.println(finalarr_test.length + "finalarr_test");
                    final_matrix_arraylist_test.add(f_test);
                   }
                    for(int k=0;k<=2;k++)
                        {
                    for(int j=0;j<=2;j++)
                        {
                          double fmv_test=applet.final_rot_matrix_test.getElement(k, j);
                          final_matrix_values_test.add(fmv_test);
                  }
                        }
                 //   System.out.println("true");

                    }
                });
             Thread.currentThread().sleep(49000);
            
            }
             
             btn2.addActionListener((ActionEvent e) -> {
              
             //   System.out.println("I AM PRESSED ONCE");
              //  count=0;
//                System.out.println("Timer task stopped at" + new Date());
//                System.out.println("Timer task stopped at" + new Date());
//                
                dict.put("TIME", time);
                dict.put("participant_data", user_data);
                dict.put("template_init_orientations",template_list_of_init_orientation );
                dict.put("template transform matrix", template_matrix_arraylist);
                dict.put("template rotation matrix 3x3 values", template_matrix_values);
                         
          //      dict.put("final_orientations", arraylist);
            
                dict.put("init_orientations",list_of_init_orientation );
                dict.put("initial transform matrix", initial_matrix_arraylist);
                dict.put("initial rotation matrix 3x3", initial_matrix_values);
                 
                 
//                 
//                dict.put("final transform matrix", final_matrix_arraylist);
//                dict.put("final rotation matrix 3x3", final_matrix_values);

                    
                dict.put("final transform matrix test in save button", final_matrix_arraylist_test);
                dict.put("final rotation matrix 3x3 in save button", final_matrix_values_test);
                 
                dict.put("xyz points", xyz_points);
                dict.put("angles", angles);
                ArrayList<ArrayList<Double>> a =  new ArrayList<>();
                a.add(angles.get(0));
                a.add(angles.get(1));
                a.add(angles.get(2));
                a.add(angles.get(3)); 
                a.add(angles.get(5));
                a.add(angles.get(6)); 
                
                PrintWriter outFile=null;
                FileWriter ioFile = null;
                try {
                    ioFile = new FileWriter( "output_3D.txt" ,true);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                outFile = new PrintWriter( ioFile );
                
               // outFile.printf( "target_score "+target_score );
                outFile.printf( "dictionary ,"+dict );
                // outFile.printf( "target_score "+target_score );
                outFile.close();

                      try  ( PrintWriter writer = new PrintWriter(new FileWriter("test.csv", true));) {

                    StringBuilder sb = new StringBuilder();
                    sb.append("Participant");
                    sb.append(',');
                    sb.append("Condition");
                    sb.append(',');            
                    //sb.append('\n');
                    sb.append("Block");
                    sb.append(',');
                    sb.append("Task");
                     sb.append(",");
                    //sb.append('\n');
                    sb.append("Trial");
                    sb.append(',');
                    sb.append("Temp.x");
                     sb.append(',');
                    //sb.append('\n');
                    sb.append("Temp.y");
                    sb.append(',');
                    sb.append("Temp.z");
                    sb.append(',');
                    sb.append("Final.x"); 
                    sb.append(',');
                    //sb.append('\n');
                    sb.append("Final.y");
                    sb.append(',');
                    sb.append("Final.z");
                     sb.append(',');
                   // sb.append('\n');
                    sb.append("Start Time");
                    sb.append(',');
                    sb.append("End Time");
                   // sb.append('\n');
                     sb.append(',');
                    sb.append("Difftime");
                 
                    sb.append('\n');
                    int k =0;
                    
                    for (int i = 1; i<=3;i++){
                    sb.append(Participant_id);
                    sb.append(',');
                    sb.append(technique_name);
                    sb.append(',');
                    sb.append(task_blockname);
                     sb.append(',');
                    sb.append(individual_taskname);
                    sb.append(',');
                    sb.append(i);
                    sb.append(',');
                    sb.append(a.get(k).get(0));
                    sb.append(',');
                    sb.append(a.get(k).get(1));
                    sb.append(',');
                    sb.append(a.get(k).get(2));
                    sb.append(',');
                    sb.append(a.get(k+1).get(0));
                    sb.append(',');
                    sb.append(a.get(k+1).get(1));
                    sb.append(',');
                    sb.append(a.get(k+1).get(2));
                    sb.append(',');
                    sb.append(time.get(k));
                    sb.append(',');
                    sb.append(time.get(k+1));
                    sb.append(',');
                    sb.append(time.get(k+1).getTime()-time.get(k).getTime());
                    sb.append(',');
                    sb.append('\n');
                    k+=2;
                  }

                    writer.write(sb.toString());

             //       System.out.println("done!");

            
            }   catch (IOException ex) {
                    Logger.getLogger(exp1.class.getName()).log(Level.SEVERE, null, ex);
                }

                
                
//                for(Frame f: list_of_frames){
//                  f.setVisible(false);
//                }
                  for(timer3d t3d: list_of_timers){
                  t3d.setVisible(false);
                }
                    for(Frame f: list_of_frames){
                  f.setVisible(false);
                }
                        pickandmove.task3d_cube_rot_mainmethod();
                
                time.clear();
                user_data.clear();
                list_of_timers.clear();
                initial_matrix_arraylist.clear();
                initial_matrix_values.clear();  
                template_matrix_arraylist.clear();
                template_matrix_values.clear();
                final_matrix_arraylist_test.clear();
                final_matrix_values_test.clear();  
                xyz_points.clear();
                angles.clear();
                  //this.setVisible(false);
               // pickandmove.task3d_cube_rot_mainmethod();
                  

                //  System.exit(0);
            });
         
//            test_btn.addActionListener(new ActionListener(){
//                    
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
////                                        for(Frame f: list_of_frames){
////                  f.setVisible(false);
////                }
////                        pickandmove.task3d_cube_rot_mainmethod();
//                  
//                      //  System.out.println("I am pressed in test button " + new Date());
//
//
//                    }
//                    
//                });
//                
//        //    }
//         //    save_btn.setSelected(true);
              }
         

	public static void main(String[] args) throws InterruptedException {
                   //mainmethod();
	}

	

    @Override
    public void mousePressed(MouseEvent e) {
    System.out.println("hi i am in pressed");
     //   t3d.set(initialaxis);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
     
        System.out.println("hi i am in releases");
//        t3d.get(finalarr);
//        t3d.get(final_rot_matrix);
    }

    public void MouseRotate(){
        
    }
    @Override
    public void mouseEntered(MouseEvent e) {
         
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      
    }
    int previousx = 0;
    int previousy = 0;
    @Override
    public void mouseDragged(MouseEvent e) {
        int currentx = e.getX();
        int currenty=e.getY();
        if(currentx>previousx){
        //  System.out.println(e.getX() + "less than 0");
                        t3dstep.rotY(Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);
    }
        else if(currentx<previousx){
        //      System.out.println(e.getX()+ "more than zero");
            t3dstep.rotY( -Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);
        }
         previousx = currentx;
         
         if(currenty>previousy){
           t3dstep.rotX(-Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);
         }
         else if(currenty<previousy){
          t3dstep.rotX(Math.PI / 32);
			tg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tg.setTransform(t3d);
         }
         previousy = currenty;  

        }
    
    
    @Override
    public void mouseMoved(MouseEvent e) {

    }


 

}
