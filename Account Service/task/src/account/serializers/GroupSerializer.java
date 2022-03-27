package account.serializers;

import account.model.Group;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GroupSerializer extends StdSerializer<Group> {

    private static final long serialVersionUID = 1L;

    public GroupSerializer() {
        this(null);
    }

    protected GroupSerializer(Class<Group> t) {
        super(t);
    }

    @Override
    public void serialize(Group value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            if (value == null) {
                gen.writeNull();
            }
            else {
                gen.writeString("ROLE_" + value.getCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
