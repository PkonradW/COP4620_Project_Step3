import java.util.*;
import java.io.*;
/**
 * <>extending the LittleBaseListener</>
 * enter program
 * enter string decl
 * ctx object has the good stuff
 * can get info about children from parent if you need (probably bad)
 *
 */
public class SimpleTableBuilder extends LittleBaseListener{
    @Override public void enterProgram(LittleParser.ProgramContext ctx) {
        System.out.println(ctx.id().IDENTIFIER().toString());
    }

}
