/* 
* The code below is written by understanding the concept of Java 3D from various soruces like 
* The Java 3D Site at Sun - http://www.sun.com/desktop/java3d
* The Java 3D Repository - http://java3d.sdsc.edu 
* The Java 3D specification - http://www.javasoft.com/products/java-media/3D/
* The Java 3D API Specification book by Henry Sowizral, Kevin Rushforth, Michael Deering and published by Addison-Wesley
* The 3D model of the Strawberry is downloaded from Google Poly and Blender was used to edit the model.
* @author sadya 
*/
package pkg3dmodel;
import java.applet.*;
import java.awt.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.behaviors.keyboard.*;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;

//import java.awt.event.KeyListener;
//import java.awt.event.KeyEvent;

import java.util.*;
import com.sun.j3d.utils.geometry.ColorCube;

import com.sun.j3d.utils.picking.*;
import com.sun.j3d.utils.picking.behaviors.*;
import com.sun.j3d.utils.picking.behaviors.PickZoomBehavior;
import com.sun.j3d.utils.picking.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.picking.behaviors.PickRotateBehavior;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import com.sun.j3d.utils.geometry.Text2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pkg3dmodel.Experiment_window.Participant_id;
import static pkg3dmodel.Experiment_window.technique_name;

public class pickandmove extends Applet implements MouseMotionListener {
    public SimpleUniverse universe = null;
	public Canvas3D canvas = null;
	public TransformGroup viewtrans = null;

        public TransformGroup tg2 = null; //for text2D
	public Transform3D t3d2 = null;
        
	public TransformGroup tg = null;
	public Transform3D t3d = null;
	public Transform3D t3dstep = new Transform3D();

	public TransformGroup tg_2 = null;
	public Transform3D t3d_2 = null;
	public Transform3D t3dstep_2 = new Transform3D();

	public TransformGroup tg_3 = null;
	public Transform3D t3d_3 = null;
	public Transform3D t3dstep_3 = new Transform3D();
        public TransformGroup btg2 = null;
	public Transform3D bt3d2 = null;
	public Matrix4d matrix = new Matrix4d();
        public Matrix3d cube1_initial_rot_matrix = new Matrix3d();
        public Matrix3d cube2_initial_rot_matrix = new Matrix3d(); 
        public Matrix3d cube3_initial_rot_matrix = new Matrix3d();
          
        public static Matrix3d cube1_final_rot_matrix = new Matrix3d();
        public static Matrix3d cube2_final_rot_matrix = new Matrix3d(); 
        public static Matrix3d cube3_final_rot_matrix = new Matrix3d();  
	public static JButton btn = new JButton ("Click to Stop");
        public static JButton finish_btn = new JButton ("Finish");
        public static JButton start_btn = new JButton ("Start");
        
        public JButton btn2 = new JButton ("Rotate");
        public static JRadioButton rbtn1=new JRadioButton("Translate");
        
      
        public static JRadioButton rb2=new JRadioButton("Rotate");
        ButtonGroup bG = new ButtonGroup();
        public Text2D text2d;
	public String ss;
        
        public static HashMap<String,ArrayList> dict = new HashMap<String,ArrayList>();
        public static ArrayList<Double> cube1_angles =null;
        public static ArrayList<Double> cube3_angles =null;
        public static ArrayList<Double> initial_angles =null;

        public static ArrayList<String> user_data = new ArrayList<>();
        static int icount = 1;
	public pickandmove() {
		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		canvas = new Canvas3D(config);
		add("Center", canvas);
                btn.setBackground(Color.CYAN);
                btn.setPreferredSize(new Dimension(50,50));
                add("South", btn);
                finish_btn.setBackground(Color.CYAN);
                finish_btn.setPreferredSize(new Dimension(70,70));
                add("East", finish_btn);
                start_btn.setBackground(Color.CYAN);
                start_btn.setPreferredSize(new Dimension(65,65));
                add("West", start_btn);

                universe = new SimpleUniverse(canvas);
                BranchGroup scene = createSceneGraph();
		universe.getViewingPlatform().setNominalViewingTransform();

		universe.getViewer().getView().setBackClipDistance(100.0);
                
		universe.addBranchGraph(scene);
                
                canvas.addMouseMotionListener(this);
	
	}


	public BranchGroup createSceneGraph() {
		BranchGroup objRoot = new BranchGroup();

		BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);

		viewtrans = universe.getViewingPlatform().getViewPlatformTransform();
               // MouseRotate mouseRot = new MouseRotate(viewtrans);
		KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewtrans);
		keyNavBeh.setSchedulingBounds(bounds);
		PlatformGeometry platformGeom = new PlatformGeometry();
		platformGeom.addChild(keyNavBeh);
                // platformGeom.addChild(mouseRot);
		universe.getViewingPlatform().setPlatformGeometry(platformGeom);
               
		objRoot.addChild(createPickMouse());
                objRoot.addChild(createText2D());
             
              
              //  objRoot.addChild(createBox());
		return objRoot;
	}

	public BranchGroup createPickMouse() {

		BranchGroup objRoot = new BranchGroup();

		BranchGroup objRoot_1 = new BranchGroup();
		BranchGroup objRoot_2 = new BranchGroup();
		BranchGroup objRoot_3 = new BranchGroup();

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0, 0.0), 300f);

                Text2D t = new Text2D("Click here" , new Color3f(0.9f, 1.0f, 1.0f), "Helvetica", 29, Font.ITALIC);
		tg = new TransformGroup();
		t3d = new Transform3D();

		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		t3d.setTranslation(new Vector3d(-5.0, 0.0, 0.0));
		t3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
		t3d.setScale(1.0);
                t3d.get(cube1_initial_rot_matrix);   // left cube
                initial_angles=calculateeulerangles(cube1_initial_rot_matrix);
		tg.setTransform(t3d);
                
		tg.addChild(new ColorCube());

		tg_2 = new TransformGroup();
		t3d_2 = new Transform3D();

		tg_2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg_2.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
                tg_2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		t3d_2.setTranslation(new Vector3d(0.0, 0.0, 0.0));
		t3d_2.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, 3.14f));
		t3d_2.setScale(0.5);
                t3d_2.get(cube2_initial_rot_matrix); // middle cube
		tg_2.setTransform(t3d_2);
                tg_2.addChild(t);
		tg_2.addChild(new ColorCube());
                
		tg_3 = new TransformGroup();
		t3d_3 = new Transform3D();

		tg_3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg_3.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
                tg_3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		t3d_3.setTranslation(new Vector3d(5.0, 0.0, 0.0));
		t3d_3.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0f));
		t3d_3.setScale(1.0);
                t3d_3.get(cube3_initial_rot_matrix); // right cube
		tg_3.setTransform(t3d_3);

		tg_3.addChild(new ColorCube());
                
		PickZoomBehavior zoom = new PickZoomBehavior(objRoot_1, canvas, bounds);
		PickZoomBehavior zoom_2 = new PickZoomBehavior(objRoot_2, canvas, bounds);
		PickZoomBehavior zoom_3 = new PickZoomBehavior(objRoot_3, canvas, bounds);
             
		objRoot_1.addChild(zoom);
		objRoot_2.addChild(zoom_2);
		objRoot_3.addChild(zoom_3);

//		PickRotateBehavior rotate = new PickRotateBehavior(objRoot_1, canvas, bounds);
//		PickRotateBehavior rotate_2 = new PickRotateBehavior(objRoot_2, canvas, bounds);
//		PickRotateBehavior rotate_3 = new PickRotateBehavior(objRoot_3, canvas, bounds);
//		objRoot_1.addChild(rotate);
//		objRoot_2.addChild(rotate_2);
//		objRoot_3.addChild(rotate_3);
//               
//              
//		PickTranslateBehavior translate = new PickTranslateBehavior(objRoot_1, canvas, bounds);
//		PickTranslateBehavior translate_2 = new PickTranslateBehavior(objRoot_2, canvas, bounds);
//		PickTranslateBehavior translate_3 = new PickTranslateBehavior(objRoot_3, canvas, bounds);
//                
//		objRoot_1.addChild(translate);
//		objRoot_2.addChild(translate_2);
//		objRoot_3.addChild(translate_3);

		objRoot_1.addChild(tg);
		objRoot_2.addChild(tg_2);
		objRoot_3.addChild(tg_3);

		objRoot.addChild(objRoot_1);
		objRoot.addChild(objRoot_2);
		objRoot.addChild(objRoot_3);

		objRoot.addChild(createLight());

		objRoot.compile();

		return objRoot;

	}
            public BranchGroup createText2D() {

            BranchGroup objRoot = new BranchGroup();
            tg2 = new TransformGroup();
            t3d2 = new Transform3D();

            t3d2.setTranslation(new Vector3d(-3.5, 4.5, 0.0));
            t3d2.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
            t3d2.setScale(5.0);
            tg2.setTransform(t3d2);
    
            //ss = this.ShuffleList();
            text2d = new Text2D("This is the rotation mode" , new Color3f(0.9f, 1.0f, 1.0f), "Helvetica", 29, Font.ITALIC);

            tg2.addChild(text2d);
            objRoot.addChild(tg2);

            objRoot.compile();

            return objRoot;

	}
   
	public Light createLight() {
		DirectionalLight light = new DirectionalLight(true, new Color3f(1.0f, 1.0f, 1.0f),
				new Vector3f(-0.3f, 0.2f, -1.0f));

		light.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

		return light;
	}
          public ArrayList<Double> calculateeulerangles(Matrix3d m){
            ArrayList<Double> a = new ArrayList<Double>();
            
            double yaw=Math.toDegrees(java.lang.Math.atan2(m.getElement(1, 0),(m.getElement(0, 0)))); // z - axis
            double pitch=Math.toDegrees(java.lang.Math.atan2(-m.getElement(2, 0),Math.sqrt(Math.pow(m.getElement(2, 1),2)+Math.pow(m.getElement(2, 2),2))));
            double roll=Math.toDegrees(java.lang.Math.atan2(m.getElement(2, 1),m.getElement(2, 2)));  //x-axis
            
            a.add(yaw);
            a.add(pitch);
            a.add(roll);
            return a;
        }
          
         public static Matrix3d AngleofRotation_Matrix(Matrix3d m, Matrix3d k ){
           Matrix3d result = new Matrix3d();
           result.mulTransposeRight(m,k);
           
           return result;
        }
        
        public static double AngleofRotation(Matrix3d result ){
          
            ArrayList<Double> result_matrix_values = new ArrayList<Double>();
            double tr = 0;    
            double rmv =0;
            double aofr =0;
               
            for(int i=0;i<=2;i++)
                          {
                      for(int j=0;j<=2;j++)
                          {
                            rmv=result.getElement(i, j);
                            result_matrix_values.add(rmv);
              }
                          }
            for (int el = 0; el<= result_matrix_values.size(); el+=4){
                tr+=result_matrix_values.get(el);
            }
            aofr = Math.acos((tr-1)/2);
            return aofr;
        }
        public static void task3d_cube_rot_mainmethod(){

                String task_blockname = "3D Task";
                String individual_taskname = "3D Cube Rotation";
            
                pickandmove p = new pickandmove();
		Frame frame = new MainFrame(p, 1350, 650);
                user_data.add(Participant_id);
                user_data.add(technique_name);
                user_data.add(task_blockname);
                user_data.add(individual_taskname);
               
                ArrayList<Date> time = new ArrayList<>();
//                Date start_d = new Date();
//                time.add(start_d);
                
                ArrayList<Double> cube1_initial_matrix_values = new ArrayList<Double>();
                ArrayList<Double> cube2_initial_matrix_values = new ArrayList<Double>();
                ArrayList<Double> cube3_initial_matrix_values = new ArrayList<Double>();
                
                ArrayList<Double> cube1_final_matrix_values = new ArrayList<Double>();
                ArrayList<Double> cube2_final_matrix_values = new ArrayList<Double>();
                ArrayList<Double> cube3_final_matrix_values = new ArrayList<Double>();
                
                ArrayList<Double> cube1_angle_of_rot_matrix_values = new ArrayList<Double>();
                ArrayList<Double> cube3_angle_of_rot_matrix_values = new ArrayList<Double>();
                ArrayList<Double> angle_of_rots = new ArrayList<Double>();
                ArrayList<ArrayList<Double>> angles = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                //Matrix3d cube1_ang_rotmatrix = null;
                
                
                double mv1 =0;
                double mv2 =0;
                double mv3=0;
                for(int i=0;i<=2;i++)
                          {
                      for(int j=0;j<=2;j++)
                          {
                            mv1=p.cube1_initial_rot_matrix.getElement(i, j);
                            cube1_initial_matrix_values.add(mv1);
              }
                          }
                       for(int i=0;i<=2;i++)
                          {
                      for(int j=0;j<=2;j++)
                          {
                            mv2=p.cube2_initial_rot_matrix.getElement(i, j);
                            cube2_initial_matrix_values.add(mv2);
              }
                          }
                       for(int i=0;i<=2;i++)
                          {
                      for(int j=0;j<=2;j++)
                          {
                            mv3=p.cube3_initial_rot_matrix.getElement(i, j);
                            cube3_initial_matrix_values.add(mv3);
              }
                          }
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
                    sb.append("EulerA_Initial.tx");
                    sb.append(',');
                    sb.append("EulerA1_Initial.ty"); 
                    sb.append(',');
                    //sb.append('\n');
                    sb.append("EulerA1_Initial.tz");
                    sb.append(',');
                    sb.append("EulerA1.tx");
                    sb.append(',');
                    sb.append("EulerA1.ty"); 
                    sb.append(',');
                    //sb.append('\n');
                    sb.append("EulerA1.tz");
                    sb.append(',');
                   sb.append("EulerA3.tx");
                    sb.append(',');
                    sb.append("EulerA3.ty"); 
                    sb.append(',');
                    //sb.append('\n');
                    sb.append("EulerA3.tz");
                    sb.append(',');
                    sb.append("Cube1AngleofRot");
                    sb.append(',');
                     sb.append("Cube3AngleofRot");
                    sb.append(',');
                    sb.append("Start Time");
                    sb.append(',');
                    sb.append("End Time");
                   // sb.append('\n');
                    sb.append(',');
                    sb.append("Difftime");
                 
                    sb.append('\n');
               
                dict.put(" cube 1 initial rotation matrix 3x3", cube1_initial_matrix_values);
                dict.put(" cube 2 initial rotation matrix 3x3", cube2_initial_matrix_values);
                dict.put(" cube 3 initial rotation matrix 3x3", cube3_initial_matrix_values);
                btn.addActionListener(new ActionListener(){

                     @Override
                     public void actionPerformed(ActionEvent e) {
                         System.out.println("I am pressed here ");
                         frame.setVisible(false);
                         user_data.clear();
                         time.clear();
                         cube1_final_matrix_values.clear();
                         cube2_final_matrix_values.clear();
                         cube3_final_matrix_values.clear();
                         pickandmove2.task3d_cube_trans_mainmethod();
//                         pickandmove2 p2 = new pickandmove2();
//                         Frame frame2 = new MainFrame(p2, 1350, 650);
                     }
                    
                });
                
                
                
                start_btn.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                        Date start_d = new Date();
                        time.add(start_d);
                        System.out.println("Timer task started at" + new Date());
                    }
                    
                });
                
                finish_btn.addActionListener(new ActionListener(){

                     @Override
                     public void actionPerformed(ActionEvent e) {
                         Matrix3d cube1_ang_rotmatrix = null;
                         Matrix3d cube3_ang_rotmatrix = null;
                         Date end_d = new Date();
                         time.add(end_d);
                        System.out.println("I am pressed - finished button");
                        dict.put("TIME", time);
                         
                        p.t3d.get(cube1_final_rot_matrix);
                        p.t3d_2.get(cube2_final_rot_matrix);
                        p.t3d_3.get(cube3_final_rot_matrix);
                        
                        
                        cube1_angles = p.calculateeulerangles(cube1_final_rot_matrix);
                        cube3_angles = p.calculateeulerangles(cube3_final_rot_matrix);
                        
                        for(int i=0;i<=2;i++)
                          {
                          for(int j=0;j<=2;j++)
                          {
                            double f_mv1=p.cube1_final_rot_matrix.getElement(i, j);
                            cube1_final_matrix_values.add(f_mv1);
                          }
                          }
                        
                           for(int i=0;i<=2;i++)
                          {
                          for(int j=0;j<=2;j++)
                          {
                            double f_mv2=p.cube2_final_rot_matrix.getElement(i, j);
                            cube2_final_matrix_values.add(f_mv2);
                          }
                          }
                           
                              for(int i=0;i<=2;i++)
                          {
                          for(int j=0;j<=2;j++)
                          {
                            double f_mv3=p.cube3_final_rot_matrix.getElement(i, j);
                            cube3_final_matrix_values.add(f_mv3);
                          }
                          }
                        // Angle of rotation matrixes
                        cube1_ang_rotmatrix = p.AngleofRotation_Matrix(p.cube1_initial_rot_matrix,cube1_final_rot_matrix);
                        cube3_ang_rotmatrix = p.AngleofRotation_Matrix(p.cube3_initial_rot_matrix,cube3_final_rot_matrix);
                        
                        for(int i=0;i<=2;i++)
                          {
                          for(int j=0;j<=2;j++)
                          {
                            double aor_mv1=cube1_ang_rotmatrix.getElement(i, j);
                            cube1_angle_of_rot_matrix_values.add(aor_mv1);
                          }
                          }
                         for(int i=0;i<=2;i++)
                          {
                          for(int j=0;j<=2;j++)
                          {
                            double aor_mv3=cube3_ang_rotmatrix.getElement(i, j);
                            cube3_angle_of_rot_matrix_values.add(aor_mv3);
                          }
                          }
                        
                        
                        // Angle of rotations
                        double aofr1 = AngleofRotation(cube1_ang_rotmatrix);
                        double aofr3 = AngleofRotation(cube3_ang_rotmatrix);
                        angle_of_rots.add(aofr1);
                        angle_of_rots.add(aofr3);
                        
                        angles.add(cube1_angles);
                        angles.add(cube3_angles);
                        angles.add(initial_angles);
                        dict.put("Participant user data", user_data);
                        dict.put("cube 1 final rotation matrix 3x3 in finish button", cube1_final_matrix_values);
                        dict.put("cube 2 final rotation matrix 3x3 in finish button", cube2_final_matrix_values);
                        dict.put("cube 3 final rotation matrix 3x3 in finish button", cube3_final_matrix_values);
                        dict.put("angles", angles);
                        dict.put("cube 1 angles",cube1_angles );
                        dict.put("cube 3 angles",cube3_angles );
                        dict.put("cube 1 angleofrot matrix ", cube3_angle_of_rot_matrix_values );
                        dict.put("cube 3 angleofrot matrix",cube3_angle_of_rot_matrix_values);
                        dict.put("angle_of_rotation",angle_of_rots );
                    //    dict.put("cube 3 angle_of_rotation",cube3_angles );
                         PrintWriter outFile=null;
                         FileWriter ioFile = null;
                        try {
                            ioFile = new FileWriter( "output_3D_cube_rotation.txt" ,true);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        outFile = new PrintWriter( ioFile );

                       // outFile.printf( "target_score "+target_score );
                        outFile.printf( "dictionary ,"+dict );
                        // outFile.printf( "target_score "+target_score );
                        outFile.close();
                        
                        
                         try  ( PrintWriter writer = new PrintWriter(new FileWriter("test.csv", true));) {
                    
                   if(icount==1 && angles.size()==3){
                    sb.append(Participant_id);
                    sb.append(',');
                    sb.append(technique_name);
                    sb.append(',');
                    sb.append(task_blockname);
                     sb.append(',');
                    sb.append(individual_taskname);
                    sb.append(',');
                    sb.append(icount);
                    sb.append(',');
                    sb.append(0);
                    sb.append(',');
                    sb.append(-0);
                    sb.append(',');
                    sb.append(0);
                    sb.append(',');
                    sb.append(dict.get("cube 1 angles").get(2));
                    sb.append(',');
                    sb.append(dict.get("cube 1 angles").get(1));
                    sb.append(',');
                    sb.append(dict.get("cube 1 angles").get(0));
                    sb.append(',');
                    sb.append(dict.get("cube 3 angles").get(2));
                    sb.append(',');
                    sb.append(dict.get("cube 3 angles").get(1));
                    sb.append(',');
                    sb.append(dict.get("cube 3 angles").get(0));
                    sb.append(',');
                    sb.append(dict.get("angle_of_rotation").get(0));
                    sb.append(',');
                    sb.append(dict.get("angle_of_rotation").get(1));
                    sb.append(',');
                    sb.append(time.get(0));
                    sb.append(',');
                    sb.append(time.get(1));
                    sb.append(',');
                    sb.append(time.get(1).getTime()-time.get(0).getTime());
                    sb.append(',');
                    
                    sb.append('\n');
                     //  sb.append('\n');
                   }
                   else{
                    sb.append(Participant_id);
                    sb.append(',');
                    sb.append(technique_name);
                    sb.append(',');
                    sb.append(task_blockname);
                     sb.append(',');
                    sb.append(individual_taskname);
                    sb.append(',');
                    sb.append(icount);
                    sb.append(',');
                    sb.append(angles.get(2).get(0));
                    sb.append(',');
                    sb.append(angles.get(2).get(1));
                    sb.append(',');
                    sb.append(angles.get(2).get(2));
                    sb.append(',');
                    sb.append(dict.get("cube 1 angles").get(2));
                    sb.append(',');
                    sb.append(dict.get("cube 1 angles").get(1));
                    sb.append(',');
                    sb.append(dict.get("cube 1 angles").get(0));
                    sb.append(',');
                    sb.append(dict.get("cube 3 angles").get(2));
                    sb.append(',');
                    sb.append(dict.get("cube 3 angles").get(1));
                    sb.append(',');
                    sb.append(dict.get("cube 3 angles").get(0));
                    sb.append(',');
                    sb.append(dict.get("angle_of_rotation").get(2));
                    sb.append(',');
                    sb.append(dict.get("angle_of_rotation").get(3));
                    sb.append(',');
                    sb.append(time.get(2));
                    sb.append(',');
                    sb.append(time.get(3));
                    sb.append(',');
                    sb.append(time.get(3).getTime()-time.get(2).getTime());
                    sb.append(',');
                    
                    sb.append('\n');
            //           sb.append('\n');
                   }
                    icount++;
                    writer.write(sb.toString());
 }   catch (IOException ex) {
                    Logger.getLogger(exp1.class.getName()).log(Level.SEVERE, null, ex);
                }  
                     }
                    
                }); 
        }

	public static void main(String[] args) {
         //         pickandmove.task3d_cube_rot_mainmethod();

	}
        
    int previousx = 0;
    int previousy = 0;
    int previousx2 = 0;
    int previousy2 = 0;
    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.isMetaDown()) {
        int currentx2 = e.getX();
        int currenty2=e.getY();
        if(currentx2>previousx2){
                t3dstep_3.rotY(Math.PI / 32);
                tg_3.getTransform(t3d_3);
                t3d_3.get(matrix);
           //     t3d_3.setTranslation(new Vector3d(0.0, 0.0, 0.0));
                t3d_3.mul(t3dstep_3);
            //    t3d_3.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
                tg_3.setTransform(t3d_3);
//                
//                 t3dstep_2.set(new Vector3d(1, 0.0, 0.0));
//                tg_2.getTransform(t3d_2);
//                t3d_2.mul(t3dstep);
//                tg_2.setTransform(t3d_2);
    }
        else if(currentx2<previousx2){
            t3dstep_3.rotY(-Math.PI / 32);
            tg_3.getTransform(t3d_3);
            t3d_3.get(matrix);
         //   t3d_3.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d_3.mul(t3dstep_3);
         //   t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg_3.setTransform(t3d_3);
        }
         previousx2 = currentx2;
         
         if(currenty2>previousy2){
            t3dstep_3.rotX(Math.PI / 32);
            tg_3.getTransform(t3d_3);
            t3d_3.get(matrix);
       //     t3d_3.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d_3.mul(t3dstep_3);
         //   t3d_3.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg_3.setTransform(t3d_3);
         }
         else if(currenty2<previousy2){
        //  t3dstep.rotX(-Math.PI / 32);
            t3dstep_3.rotX(-Math.PI / 32);
            tg_3.getTransform(t3d_3);
            t3d_3.get(matrix);
       //     t3d_3.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d_3.mul(t3dstep_3);
       //     t3d_3.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg_3.setTransform(t3d_3);
         }
         previousy2 = currenty2;  
    }
        else{
      //  System.out.println(e.BUTTON3_DOWN_MASK);
  //      }
        System.out.println(e.getX() + "less than 0");
        
        int currentx = e.getX();
        int currenty=e.getY();
        if(currentx>previousx){
            t3dstep.rotY(Math.PI / 32);
            tg.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg.setTransform(t3d);

    }
        else if(currentx<previousx){
            t3dstep.rotY(-Math.PI / 32);
            tg.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg.setTransform(t3d);
        }
         previousx = currentx;
         
         if(currenty>previousy){
            t3dstep.rotX(Math.PI / 32);
            tg.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg.setTransform(t3d);
         }
         else if(currenty<previousy){
            t3dstep.rotX(-Math.PI / 32);
            tg.getTransform(t3d);
            t3d.get(matrix);
            t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
            t3d.mul(t3dstep);
            t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
            tg.setTransform(t3d);
         }
         previousy = currenty;   
    }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
