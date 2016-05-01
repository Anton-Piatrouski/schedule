import java.util.List;

import by.epam.beans.Flight;
import by.epam.xmlshedule.XmlUtils;

public class Runner {

	public static void main(String[] args) {
		List<Flight> flights = XmlUtils.readScheduleFromFile("src/schedule.xml");
		XmlUtils.exportScheduleToXML("src/exportedSchedule.xml", flights);
		
		for (Flight flight : flights) {
			System.out.println(flight);
		}
	}

}
