/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.script.dom.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateModelObject;

import junit.framework.JUnit4TestAdapter;


/**
 * DiagramModelGroupProxyTests Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class DiagramModelGroupProxyTests extends DiagramModelObjectProxyTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelGroupProxyTests.class);
    }
    
    private ArchimateModelProxy modelProxy;
    private ArchimateDiagramModelProxy viewProxy;
    
    @Before
    public void runOnceBeforeEachTest() {
        modelProxy = TestsHelper.createTestModel();
        viewProxy = modelProxy.createArchimateView("test");
        testProxy = viewProxy.createObject("group", 0, 0, 100, 100);
        testEObject = (IArchimateModelObject)testProxy.getEObject();
        actualTestProxy = (DiagramModelGroupProxy)testProxy;
    }

    @Override
    @Test
    public void get_ReturnsCorrectProxy() {
        EObjectProxy proxy = EObjectProxy.get(testEObject);
        assertTrue(proxy instanceof DiagramModelGroupProxy);
    }

    @Override
    @Test
    public void getReferencedConcept() {
        assertNull(actualTestProxy.getConcept());
    }
    
    @Override
    @Test
    public void children() {
        assertTrue(testProxy.children().isEmpty());
        
        DiagramModelObjectProxy noteProxy = ((DiagramModelObjectProxy)testProxy).createObject("note", 0, 0, 20, 20);
        assertEquals(1, testProxy.children().size());
        assertEquals(noteProxy, testProxy.children().first());
    }
    
    @Override
    @Test
    public void delete() {
        assertEquals(viewProxy, testProxy.parent());
        testProxy.delete();
        assertNull(testProxy.parent());
    }
    
    @Override
    @Test
    public void parent() {
        assertEquals(viewProxy, testProxy.parent());
    }

    @Override
    @Test
    public void parents() {
        assertNotNull(testProxy.parents());
    }

    @Test
    public void attr_BorderType() {
        assertEquals(0, actualTestProxy.attr(IModelConstants.BORDER_TYPE));
        actualTestProxy.attr(IModelConstants.BORDER_TYPE, 1);
        assertEquals(1, actualTestProxy.attr(IModelConstants.BORDER_TYPE));
    }

}