package org;
import java.io.File;

/**
 * Outils divers
 * @author Martin Wetterwald
 *
 */
public class Utils
{
    /**
     * Récupère l'extension d'un fichier
     * @param f Fichier dont on veut savoir l'extension
     * @return L'extension du fichier
     */
    public static String getExtension(File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1)
        {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
