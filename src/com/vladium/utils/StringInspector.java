package com.vladium.utils;

public class StringInspector implements IObjectProfileNode.INodeFilter,
                                         IObjectProfileNode.INodeVisitor
{
  public boolean accept (IObjectProfileNode node)
  {
    m_node = null;
    final Object obj = node.object ();
    if ((obj != null) && (node.parent () != null))
    {
      final Object parentobj = node.parent ().object ();
      if ((obj.getClass () == char [].class) && (parentobj.getClass () == String.class))
      {
        int wasted = ((char []) obj).length - ((String) parentobj).length (); 
        if (wasted > 0)
        {
          m_node = node.parent ();
          m_wasted += m_nodeWasted = wasted;
        }
      }
    }
    return true;
  }
            
  public void previsit (IObjectProfileNode node)
  {
    if (m_node != null) 
    {
     // System.out.println (ObjectProfiler.pathName (m_node.path ())
     //                + ": " + m_nodeWasted  + " bytes wasted");
    }
  }
            
  public void postvisit (IObjectProfileNode node)
  {
   // Do nothing
  }
            
  public int wasted ()
  {
    return 2 * m_wasted;
  }
            
  private IObjectProfileNode m_node;
  private int m_nodeWasted, m_wasted;
            
  /* Veracode reporting unnecessary entry point.  Commented out main...
  public static void main(String[] args) 
  {
    IObjectProfileNode profile = ObjectProfiler.profile (args);
    StringInspector si = new StringInspector ();
    profile.traverse (si, si);
    System.out.println ("wasted " + si.wasted () + " bytes (out of " + profile.size () + ")");
  }
  */

} // End of local class

