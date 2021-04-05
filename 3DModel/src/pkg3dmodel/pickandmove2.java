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
import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.MouseButton;
import static javax.media.j3d.ColoringAttributes.FASTEST;
import static pkg3dmodel.Experiment_window.Participant_id;
import static pkg3dmodel.Experiment_window.technique_name;
import static pkg3dmodel.Main.task_blockname;

public class pickandmove2 extends Applet implements MouseMotionListener {
     private SimpleUniverse universe = null;
	private Canvas3D canvas = null;
	private TransformGroup viewtrans = null;

        private TransformGroup tg2 = null; //for text2D
	private Transform3D t3d2 = null;
        
	private TransformGroup tg = null;
	private Transform3D t3d = null;
	private Transform3D t3dstep = new Transform3D();
        private static float[] cube1_initial_transform_matrix_t3d = new float[16]; 
        private static float[] cube1_final_transform_matrix_t3d = new float[16]; 
        
	private TransformGroup tg_2 = null;
	private Transform3D t3d_2 = null;
	private Transform3D t3dstep_2 = new Transform3D();

	private TransformGroup tg_3 = null;
	private Transform3D t3d_3 = null;
	private Transform3D t3dstep_3 = new Transform3D();
        private static float[] cube3_initial_transform_matrix_t3d_3 = new float[16];
        private static float[] cube3_final_transform_matrix_t3d_3 = new float[16];
        // for template
        private TransformGroup tg_4 = null;
	private Transform3D t3d_4 = null;
	private Transform3D t3dstep_4 = new Transform3D();
        private TransformGroup tg_5 = null;
	private Transform3D t3d_5 = null;
	private Transform3D t3dstep_5 = new Transform3D();
        private static float[] template_transform_matrix_t3d_4 = new float[16]; 
        private static float[] template_transform_matrix_t3d_5 = new float[16]; 
        
        
        private TransformGroup btg2 = null;
	private Transform3D bt3d2 = null;
	private Matrix4d matrix = new Matrix4d();
        
        private static JButton btn1 = new JButton ("Exit this round of thr Experiment");
        private static JButton save_btn = new JButton ("Finish");
        private static JButton start_btn = new JButton ("Start");
        private Text2D text2d;
	private String ss;
        private static float random=0.0f;
        private static float random2=0.0f;
        static int icount = 1;
        private static HashMap<String,ArrayList> dict = new HashMap<String,ArrayList>();
        public static ArrayList<String> user_data = new ArrayList<>();

        private BranchGroup scene=null;
	public pickandmove2() {
		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		canvas = new Canvas3D(config);
		add("Center", canvas);

                btn1.setBackground(Color.CYAN);
                btn1.setPreferredSize(new Dimension(65,65));
                add("South", btn1);
                save_btn.setBackground(Color.CYAN);
                save_btn.setPreferredSize(new Dimension(70,70));
                add("East", save_btn);
                start_btn.setBackground(Color.CYAN);
                start_btn.setPreferredSize(new Dimension(65,65));
                add("West", start_btn);
		
                universe = new SimpleUniverse(canvas);
                scene = createSceneGraph2();
                scene.setCapability(BranchGroup.ALLOW_DETACH);
                scene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
                scene.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
                universe.getViewingPlatform().setNominalViewingTransform();

                universe.getViewer().getView().setBackClipDistance(100.0);

                universe.addBranchGraph(scene);
                Timer t = new Timer();
                 
                t.schedule(new TimerTask() {
                
                @Override
                public void run() {
                  // scene.removeAllChildren();
                   universe.getLocale().removeBranchGraph(scene);
                 
                   scene = createSceneGraph2();
                   scene.setCapability(BranchGroup.ALLOW_DETACH);
                   scene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
                   scene.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
                   universe.getViewingPlatform().setNominalViewingTransform();
                   
                   universe.getViewer().getView().setBackClipDistance(100.0);
                   
                   universe.addBranchGraph(scene);
                   System.out.println("Executed...");
                   
                    }
               
                  }, 1000*50,1000*50);

                canvas.addMouseMotionListener(this);
             
//                  btn1.addActionListener(new ActionListener(){
//
//                     @Override
//                     public void actionPerformed(ActionEvent e) {
//                         System.out.println("2");
//                         pickandmove p = new pickandmove();
//                         Frame frame = new MainFrame(p, 1350, 650);
//                       //  System.out.println("clicked to go back to rotate");
//                     }
//                    
//                });

        }
        private BranchGroup createSceneGraph2() {
		BranchGroup objRoot = new BranchGroup();
                objRoot.setCapability(BranchGroup.ALLOW_DETACH);
		BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);

		viewtrans = universe.getViewingPlatform().getViewPlatformTransform();

		KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewtrans);
		keyNavBeh.setSchedulingBounds(bounds);
		PlatformGeometry platformGeom = new PlatformGeometry();
		platformGeom.addChild(keyNavBeh);
		universe.getViewingPlatform().setPlatformGeometry(platformGeom);

		objRoot.addChild(createPickMouse2());
                objRoot.addChild(createText2D());
		return objRoot;
	}

	private BranchGroup createPickMouse2() {

		BranchGroup objRoot = new BranchGroup();
                objRoot.setCapability(BranchGroup.ALLOW_DETACH);
		BranchGroup objRoot_1 = new BranchGroup();
		BranchGroup objRoot_2 = new BranchGroup();
		BranchGroup objRoot_3 = new BranchGroup();
           
                BranchGroup objRoot_4 = new BranchGroup(); // for template
                BranchGroup objRoot_5 = new BranchGroup(); // for template
                objRoot_1.setCapability(BranchGroup.ALLOW_DETACH);
                objRoot_2.setCapability(BranchGroup.ALLOW_DETACH);
                objRoot_3.setCapability(BranchGroup.ALLOW_DETACH);
                objRoot_4.setCapability(BranchGroup.ALLOW_DETACH);
                objRoot_5.setCapability(BranchGroup.ALLOW_DETACH);
                
                
                
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 300f);

		tg = new TransformGroup();
		t3d = new Transform3D();

		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		t3d.setTranslation(new Vector3d(-5.0, 0.0, 0.0));
		t3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
		t3d.setScale(1.0);
                t3d.get(cube1_initial_transform_matrix_t3d);
		tg.setTransform(t3d);

		tg.addChild(new ColorCube());

		tg_2 = new TransformGroup();
		t3d_2 = new Transform3D();

		tg_2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg_2.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		t3d_2.setTranslation(new Vector3d(0.0, 0.0, 0.0));
		t3d_2.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, 3.14f));
		t3d_2.setScale(0.5);

		tg_2.setTransform(t3d_2);

		tg_2.addChild(new ColorCube());

		tg_3 = new TransformGroup();
		t3d_3 = new Transform3D();

		tg_3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg_3.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		t3d_3.setTranslation(new Vector3d(5.0, 0.0, 0.0));
		t3d_3.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0f));
		t3d_3.setScale(1.0);
                t3d_3.get(cube3_initial_transform_matrix_t3d_3);
		tg_3.setTransform(t3d_3);

		tg_3.addChild(new ColorCube());
                Random r = new Random();
                random = 2.0f + r.nextFloat() * (8.0f - 20.0f);
            
                System.out.println(random);
                Random r2 = new Random();
                random2 = 3.0f + r2.nextFloat() * (10.0f - 30.0f);
            
                System.out.println(random2); 
                         
                       // for template
                tg_4 = new TransformGroup();
		t3d_4 = new Transform3D();
                t3d_4.setScale(1.0);
                t3d_4.setTranslation(new Vector3d(-random2, random, 0.0));
		//t3d_4.setTranslation(new Vector3d(-8.0, 4.0, 0.0));
                t3d_4.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
               // t3d_4.get(template_transform_matrix_t3d_4);
		tg_4.setTransform(t3d_4);
                Appearance app = new Appearance();
                TransparencyAttributes transparencyAttributes =
                   new TransparencyAttributes(TransparencyAttributes.BLENDED,0.5f,
                   TransparencyAttributes.BLEND_SRC_ALPHA,
                   TransparencyAttributes.BLEND_ONE);
                transparencyAttributes.setTransparency(.7f);
                app.setTransparencyAttributes( transparencyAttributes );
                ColorCube cc = new ColorCube();
                cc.setAppearance(app);
		tg_4.addChild(cc);

                   // for template
                tg_5 = new TransformGroup();
		t3d_5 = new Transform3D();
                t3d_5.setScale(1);
                t3d_5.setTranslation(new Vector3d(random2, -random, 0.0));
		//t3d_5.setTranslation(new Vector3d(8.0, -4.0, 0.0));
                t3d_5.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
              //  t3d_5.get(template_transform_matrix_t3d_5);
		tg_5.setTransform(t3d_5);
                Appearance app2 = new Appearance();
                TransparencyAttributes transparencyAttributes2 =
                   new TransparencyAttributes(TransparencyAttributes.BLENDED,0.5f,
                   TransparencyAttributes.BLEND_SRC_ALPHA,
                   TransparencyAttributes.BLEND_ONE);
                transparencyAttributes2.setTransparency(.7f);
                app2.setTransparencyAttributes( transparencyAttributes2 );
                ColorCube cc2 = new ColorCube();
                cc2.setAppearance(app2);
		tg_5.addChild(cc2);
                
              
                     

                
//              MouseTranslate mouseTrans=new MouseTranslate(MouseBehavior.MANUAL_WAKEUP);
//              mouseTrans.setTransformGroup(tg_2);
//              mouseTrans.setSchedulingBounds(bounds);
		PickZoomBehavior zoom = new PickZoomBehavior(objRoot_1, canvas, bounds);
//		PickZoomBehavior zoom_2 = new PickZoomBehavior(objRoot_2, canvas, bounds);
//		PickZoomBehavior zoom_3 = new PickZoomBehavior(objRoot_3, canvas, bounds);
//             
		objRoot_1.addChild(zoom);
//		objRoot_2.addChild(zoom_2);
//		objRoot_3.addChild(zoom_3);

//		PickRotateBehavior rotate = new PickRotateBehavior(objRoot_1, canvas, bounds);
//		PickRotateBehavior rotate_2 = new PickRotateBehavior(objRoot_2, canvas, bounds);
//		PickRotateBehavior rotate_3 = new PickRotateBehavior(objRoot_3, canvas, bounds);
//		objRoot_1.addChild(rotate);
//		objRoot_2.addChild(rotate_2);
//		objRoot_3.addChild(rotate_3);

//		PickTranslateBehavior translate = new PickTranslateBehavior(objRoot_1, canvas, bounds);
		PickTranslateBehavior translate_2 = new PickTranslateBehavior(objRoot_2, canvas, bounds);
		PickTranslateBehavior translate_3 = new PickTranslateBehavior(objRoot_3, canvas, bounds);
               // translate_2.getWakeupCondition();
              
            //    objRoot_2.addChild(mouseTrans);
//		objRoot_1.addChild(translate);
		objRoot_2.addChild(translate_2);
         //       System.out.print(rotate.isLive());
		objRoot_3.addChild(translate_3);

		objRoot_1.addChild(tg);
		objRoot_2.addChild(tg_2);
		objRoot_3.addChild(tg_3);
                objRoot_4.addChild(tg_4); // for temp
                objRoot_5.addChild(tg_5); // for temp 
		objRoot.addChild(objRoot_1);
		objRoot.addChild(objRoot_2);
		objRoot.addChild(objRoot_3);
                objRoot.addChild(objRoot_4);  // for temp
                 objRoot.addChild(objRoot_5); // for temp
                
		objRoot.addChild(createLight());
         //       objRoot.addChild(translate_2);
		objRoot.compile();

		return objRoot;

	}
          private BranchGroup createText2D() {

            BranchGroup objRoot = new BranchGroup();
            tg2 = new TransformGroup();
            t3d2 = new Transform3D();

            t3d2.setTranslation(new Vector3d(-3.5, 4.5, 0.0));
            t3d2.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
            t3d2.setScale(5.0);
            tg2.setTransform(t3d2);
    
            //ss = this.ShuffleList();
            text2d = new Text2D("This is the translation mode" , new Color3f(0.9f, 1.0f, 1.0f), "Helvetica", 29, Font.ITALIC);

            tg2.addChild(text2d);
            objRoot.addChild(tg2);

            objRoot.compile();

            return objRoot;

	}
          
        
	private Light createLight() {
		DirectionalLight light = new DirectionalLight(true, new Color3f(1.0f, 1.0f, 1.0f),
				new Vector3f(-0.3f, 0.2f, -1.0f));

		light.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

		return light;
	}
        
        public static void task3d_cube_trans_mainmethod(){
       
                String task_blockname = "3D Task";
                String individual_taskname = "3D Cube Translation";
            
                user_data.add(Participant_id);
                user_data.add(technique_name);
                user_data.add(task_blockname);
                user_data.add(individual_taskname);
                ArrayList<Float> cube1_initial_matrix_values = new ArrayList<>();
                ArrayList<Float> cube1_final_matrix_values = new ArrayList<>();
                ArrayList<Float> cube1_template_matrix_values = new ArrayList<>();
                
                ArrayList<Float> cube3_initial_matrix_values = new ArrayList<>();
                ArrayList<Float> cube3_final_matrix_values = new ArrayList<>();
                ArrayList<Float> cube3_template_matrix_values = new ArrayList<>();
                ArrayList<timer3d> list_of_timers = new ArrayList<>(); 
                
         	pickandmove2 p2 = new pickandmove2();
                Frame frame2 = new MainFrame(p2, 900, 650);  
		//Frame frame2 = new MainFrame(p2, 1350, 650);  
                
                ArrayList<Date> time = new ArrayList<Date>();
               // PrintWriter writer=null;
               StringBuilder sb = new StringBuilder();
                  int i =1;
              
                for (Float init1: cube1_initial_transform_matrix_t3d){
                 //   System.out.println(initarr.length + "initarr");
                    cube1_initial_matrix_values.add(init1);
                }
                
                for (Float init3: cube3_initial_transform_matrix_t3d_3){
                 //   System.out.println(initarr.length + "initarr");
                    cube3_initial_matrix_values.add(init3);
                }
                 // try  ( writer = new PrintWriter(new FileWriter("test.csv", true));) {

                    
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
                    sb.append("Template1.tx");
                    sb.append(',');
                    sb.append("Final1.tx"); 
                    sb.append(',');
                    //sb.append('\n');
                    sb.append("Template1.ty");
                    sb.append(',');
                    sb.append("Final1.ty");
                    sb.append(',');
                    sb.append("Template3.tx");
                    sb.append(',');
                    sb.append("Final3.tx");
                    sb.append(',');
                    sb.append("Template3.ty");
                    sb.append(',');
                    sb.append("Final3.ty");
                     sb.append(',');
                    sb.append("Start Time");
                    sb.append(',');
                    sb.append("End Time");
                   // sb.append('\n');
                    sb.append(',');
                    sb.append("Difftime");
                 
                    sb.append('\n');
               
                    
                
                start_btn.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        timer3d t = new timer3d();
                        t.runTimer();  
                        t.setLocation(900, 250);
                        list_of_timers.add(t);
                        t.setVisible(true);
                        if( list_of_timers.size()==2){
                             list_of_timers.get(0).setVisible(false);
                        }
                        Date start_d = new Date();
                        time.add(start_d);
                        System.out.println("Timer task started at" + new Date());


                    }
                    
                });
             
                save_btn.addActionListener(new ActionListener(){
                     
                     @Override
                     public void actionPerformed(ActionEvent e) {
                         Date end_d = new Date();
                         time.add(end_d);
                      //  System.out.println("I am pressed - finish button");
                        dict.put("TIME", time);
                        
                        p2.t3d_4.get(template_transform_matrix_t3d_4); // cube 3 template
                        p2.t3d_5.get(template_transform_matrix_t3d_5); // cube 1 template
                        
                        for (Float temp3: template_transform_matrix_t3d_4){
                        //   System.out.println(initarr.length + "initarr");
                           cube3_template_matrix_values.add(temp3);
                       }

                       for (Float temp1: template_transform_matrix_t3d_5){
                        //   System.out.println(initarr.length + "initarr");
                           cube1_template_matrix_values.add(temp1);
                         }
                        
                        p2.t3d.get(cube1_final_transform_matrix_t3d);
                        p2.t3d_3.get(cube3_final_transform_matrix_t3d_3);
                        for (Float final1: cube1_final_transform_matrix_t3d){
                            //   System.out.println(initarr.length + "initarr");
                               cube1_final_matrix_values.add(final1);
                           }

                        for (Float final3: cube3_final_transform_matrix_t3d_3){
                            //   System.out.println(initarr.length + "initarr");
                               cube3_final_matrix_values.add(final3);
                }
                        dict.put("Participant data",user_data);
                        dict.put("cube 1 initial matrix", cube1_initial_matrix_values);
                        dict.put("cube 1 final matrix", cube1_final_matrix_values);
                        dict.put("cube 1 template matrix", cube1_template_matrix_values);
                        
                         dict.put("cube 3 initial matrix", cube3_initial_matrix_values);
                        dict.put("cube 3 final matrix", cube3_final_matrix_values);
                        dict.put("cube 3 template matrix", cube3_template_matrix_values);
                         PrintWriter outFile=null;
                         FileWriter ioFile = null;
                        try {
                            ioFile = new FileWriter( "output_3D_cube_translation.txt" ,true);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        outFile = new PrintWriter( ioFile );

                       // outFile.printf( "target_score "+target_score );
                        outFile.printf( "dictionary ,"+dict );
                        // outFile.printf( "target_score "+target_score );
                        outFile.close();
                        
                       
                     try  ( PrintWriter writer = new PrintWriter(new FileWriter("test.csv", true));) {
                    
                   if(icount==1){
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
                    sb.append(dict.get("cube 1 template matrix").get(3));
                    sb.append(',');
                    sb.append(dict.get("cube 1 final matrix").get(3));
                    sb.append(',');
                    sb.append(dict.get("cube 1 template matrix").get(7));
                    sb.append(',');
                    sb.append(dict.get("cube 1 final matrix").get(7));
                    sb.append(',');
                    sb.append(dict.get("cube 3 template matrix").get(3));
                    sb.append(',');
                    sb.append(dict.get("cube 3 final matrix").get(3));
                    sb.append(',');
                    sb.append(dict.get("cube 3 template matrix").get(7));
                    sb.append(',');
                    sb.append(dict.get("cube 3 final matrix").get(7));
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
                    sb.append(dict.get("cube 1 template matrix").get(19));
                    sb.append(',');
                    sb.append(dict.get("cube 1 final matrix").get(19));
                    sb.append(',');
                    sb.append(dict.get("cube 1 template matrix").get(23));
                    sb.append(',');
                    sb.append(dict.get("cube 1 final matrix").get(23));
                    sb.append(',');
                    sb.append(dict.get("cube 3 template matrix").get(19));
                    sb.append(',');
                    sb.append(dict.get("cube 3 final matrix").get(19));
                    sb.append(',');
                    sb.append(dict.get("cube 3 template matrix").get(23));
                    sb.append(',');
                    sb.append(dict.get("cube 3 final matrix").get(23));
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
             //       System.out.println("done!");

            
            

                     }
                    
                }); 
                //i++;
                btn1.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                   // System.out.println("I am pressed - finished button");
                    user_data.clear();
                    time.clear();
                    cube1_initial_matrix_values.clear();
                    cube1_final_matrix_values.clear();
                    cube1_template_matrix_values.clear();
                    cube3_initial_matrix_values.clear();
                    cube3_final_matrix_values.clear();
                    cube3_template_matrix_values.clear();
                       for(timer3d t3d: list_of_timers){
                  t3d.setVisible(false);
                }
                //    System.out.println("2");  
                    frame2.setVisible(false);
                    System.exit(0);
//                    Experiment_window ew = new Experiment_window();
//                    ew.setVisible(true);
//                   
                   
//                    pickandmove p = new pickandmove();
//                    Frame frame = new MainFrame(p, 1350, 650);
                  //  System.out.println("clicked to go back to rotate");
                }

             });
                
                
        }

	public static void main(String[] args) throws InterruptedException {
         // pickandmove2.task3d_cube_trans_mainmethod();
        
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
                //System.out.println(e.getX() + "less than 0");
                t3dstep_3.set(new Vector3d(0.02, 0.0, 0.0));
                tg_3.getTransform(t3d_2);
                t3d.mul(t3dstep_2);
                tg_3.setTransform(t3d_2);

    }
        else if(currentx2<previousx2){
              //System.out.println(e.getX()+ "more than zero");
                t3dstep_3.set(new Vector3d(-0.02, 0.0, 0.0));
                tg_3.getTransform(t3d_3);
                t3d_3.mul(t3dstep_3);
                tg_3.setTransform(t3d_3);
        }
         previousx2 = currentx2;
         
         if(currenty2>previousy2){
          //   System.out.println(e.getX()+ "more than zero");
                t3dstep_3.set(new Vector3d(0.0, -0.02, 0.0));
                tg_3.getTransform(t3d_3);
                t3d_3.mul(t3dstep_3);
                tg_3.setTransform(t3d_3);
         }
         else if(currenty2<previousy2){
           //  t3dstep.rotX(-Math.PI / 32);
                t3dstep_3.set(new Vector3d(0.0, 0.02, 0.0));
                tg_3.getTransform(t3d_3);
                t3d_3.mul(t3dstep_3);
                tg_3.setTransform(t3d_3);
         }
         previousy2 = currenty2;  
    }
        else{
  
        int currentx = e.getX();
        int currenty=e.getY();
        if(currentx>previousx){
                
                t3dstep.set(new Vector3d(0.05, 0.0, 0.0));
                tg.getTransform(t3d);
                t3d.mul(t3dstep);
                tg.setTransform(t3d);
    }
        else if(currentx<previousx){
              
                t3dstep.set(new Vector3d(-0.05, 0.0, 0.0));
                tg.getTransform(t3d);
                t3d.mul(t3dstep);
                tg.setTransform(t3d);
        }
         previousx = currentx;
         
         if(currenty>previousy){
            
                t3dstep.set(new Vector3d(0.0, -0.05, 0.0));
                tg.getTransform(t3d);
                t3d.mul(t3dstep);
                tg.setTransform(t3d);
         }
         else if(currenty<previousy){
        
                t3dstep.set(new Vector3d(0.0, 0.05, 0.0));
                tg.getTransform(t3d);
                t3d.mul(t3dstep);
                tg.setTransform(t3d);
         }
         previousy = currenty;   
    }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        
    }

  
    

}
