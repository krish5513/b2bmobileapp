package com.rightclickit.b2bsaleon.imageloading;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Sekhar Kuppa
 *
 */
public class Utils {
    /**
     * @param is Input stream
     * @param os Output stream
     */
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}