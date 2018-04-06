/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.script.views.file;

import java.io.File;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;


/**
 * File Tree Viewer
 */
public class FileTreeViewer extends TreeViewer {
    /**
     * The Root Folder we are exploring
     */
    private File fRootFolder;
    
    /**
     * Refresh timer
     */
    private Runnable fTimer;
    
    /**
     * Refresh timer interval of 5 seconds
     */
    static int TIMERDELAY = 5000;
    
    /**
     * Constructor
     */
    public FileTreeViewer(File rootFolder, Composite parent) {
        super(parent, SWT.MULTI);
        
        fRootFolder = rootFolder;
        
        setup();
        
        setContentProvider(new FileTreeContentProvider());
        setLabelProvider(new FileTreeLabelProvider());
        
        fRootFolder.mkdirs();
        setInput(fRootFolder);
        
        expandToLevel(ALL_LEVELS);
    }

    /**
     * Set things up.
     */
    protected void setup() {
        setupRefreshTimer();
        
        // Sort folders first, files second, alphabetical
        setComparator(new ViewerComparator() {
            @Override
            public int category(Object element) {
                if(element instanceof File) {
                    File f = (File)element;
                    return f.isDirectory() ? 0 : 1;
                }
            	return 0;
            }
        });
    }
    
    /**
     * Set up the Refresh timer
     */
    protected void setupRefreshTimer() {
        fTimer = new Runnable() {
            public void run() { 
                if(!getTree().isDisposed()) { // this is important!
                    refresh();
                    Display.getDefault().timerExec(TIMERDELAY, this);  // run again
                }
            }
        };
        
        Display.getDefault().timerExec(TIMERDELAY, fTimer);
    }
    
    /* 
     * Over-ride - make sure we have a folder!
     */
    @Override
    public void refresh() {
        fRootFolder.mkdirs();
        super.refresh();
    }
    
    /* 
     * Over-ride - make sure we have a folder!
     */
    @Override
    public void refresh(final Object element) {
        fRootFolder.mkdirs();
        super.refresh(element);
    }
    
    /**
     * Dispose of stuff
     */
    public void dispose() {
        if(fTimer != null) {
            Display.getDefault().timerExec(-1, fTimer);
            fTimer = null;
        }
    }
    
    // ===============================================================================================
	// ===================================== Tree Model ==============================================
	// ===============================================================================================
    
    /**
     * The Tree Model for the Tree.
     */
    class FileTreeContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }
        
        public Object getParent(Object child) {
            if(child instanceof File) {
                return ((File)child).getParentFile();
            }
            return null;
        }
        
        public Object [] getChildren(Object parent) {
            if(parent instanceof File) {
                return ((File)parent).listFiles();
            }
            return new Object[0];
        }
        
        public boolean hasChildren(Object parent) {
            if(parent instanceof File) {
                File f = (File)parent;
                return f.isDirectory() && f.listFiles().length > 0;
            }
            return false;
        }
    }
    
    // ===============================================================================================
	// ===================================== Label Model ==============================================
	// ===============================================================================================

    class FileTreeLabelProvider extends LabelProvider {
        
        @Override
        public String getText(Object obj) {
        	if(obj instanceof File) {
        	    File f = (File)obj;
        	    return f.getName();
        	}
        	else {
        	    return ""; //$NON-NLS-1$
        	}
        }
        
        @Override
        public Image getImage(Object obj) {
            Image image = null;
            
            if(obj instanceof File) {
                File f = (File)obj;
                if(f.isDirectory()) {
                    image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
                }
                else {
                    image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
                }
            }
            
            if(image == null) {
                image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
            }
            
            return image;
        }
    }
}
