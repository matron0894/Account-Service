package account.handlers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class APIEmployee {
    private String name;
    private String lastname;
    private String period;
    private String salary;

    private final SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");
    private final SimpleDateFormat month_year = new SimpleDateFormat("MMMMMMMMM-yyyy", Locale.ENGLISH);


    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPeriod(String period) {
        try {
            Date date = formatter.parse(period);
            this.period = month_year.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setSalary(Long salary) {
        this.salary = String.format("%d dollar(s) %d cent(s)", salary / 100, salary % 100);
    }
}
