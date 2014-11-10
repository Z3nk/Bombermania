package org;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class MapFilter extends FileFilter
{
    public MapFilter()
    {
    }

    public boolean accept(File f)
    {
        if (f.isDirectory())
            return true;
        
        String extension = Utils.getExtension(f);
        if(extension != null)
        {
            if(extension.equals(new String("bmmap")))
                return true;
        }
        
        return false;
    }

    public String getDescription()
    {
        return "BomberMania Map files only (*.bmmap)";
    }
}
