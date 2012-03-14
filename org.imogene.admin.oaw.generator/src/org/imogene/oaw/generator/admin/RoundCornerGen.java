package org.imogene.oaw.generator.admin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.xtend.expression.AbstractExpressionsUsingWorkflowComponent;
import org.imogene.model.core.CardEntity;
import org.imogene.model.core.impl.ProjectImpl;


/**
 * Generates images used in to render 
 * the rounded corner in the GWT application
 * @author Medes-IMPS
 */
public class RoundCornerGen extends AbstractExpressionsUsingWorkflowComponent {

	private static final String COMPONENT_NAME="GWT round corner generator";
	
	private String modelPath;
	
	private String genPath;
	
	private String projectName;			
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String pProjectName) {
		this.projectName = pProjectName;
	}

	public String getModelPath() {
		return modelPath;
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}

	public String getGenPath() {
		return genPath;
	}

	public void setGenPath(String pGenPath) {		
		if(!pGenPath.endsWith("/")){
			pGenPath = pGenPath+"/"; 
		}		
		genPath = pGenPath;
	}	

	@Override
	public String getComponentName() {		
		return COMPONENT_NAME;
	}
		

	@Override
	public void invokeInternal2(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {	
		Set<String> colors = new HashSet<String>();
		for(String name:ctx.getSlotNames()){			
			if(name.equals("project")){
				ProjectImpl project = (ProjectImpl)ctx.get(name);
				EList<CardEntity> entities = project.getEntities();
				for(CardEntity entity : entities){
					String color = entity.getColor();
					if(color !=null && !colors.contains(color) && color.length()==6){
						generateCorner(color, false);
						colors.add(color);
					}
				}
			}			
		}		
	}
	
	/**
	 * 
	 * @param color
	 */
	private void generateCorner(final String color, final boolean round){
		try {
			/* draw the image */
			final Display display = Display.getDefault();			
			final Image image = new Image(display, 10, 10);					
			display.asyncExec(new Runnable(){
				@Override
				public void run(){
										
					int i = Integer.parseInt(color, 16);
					Color c = new Color(Display.getDefault(), new RGB((i & 0xff0000) >> 16, (i & 0xff00) >> 8,
					 i & 0xff));
					GC gc = new GC(image);
					gc.setBackground(c);
					gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
					gc.setAntialias(SWT.ON);
					if(round){
						gc.fillOval(0, 0, 10, 10);
					}else{									
						gc.fillRectangle(0, 0, 10, 10);
					}
					
					/* save the image */
					ImageLoader loader = new ImageLoader();
					loader.data = new ImageData[] { image.getImageData() };	
					File directory = new File(genPath+projectName.toLowerCase()+"/public/images/");
					if(!directory.exists())
						directory.mkdirs();
					loader.save(directory.getAbsolutePath() + "/corner-" + color + ".png", SWT.IMAGE_PNG);
										
					c.dispose();
					gc.dispose();
					image.dispose();					
				}
			});									
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
