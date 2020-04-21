//Martin Abadzheiv S1518532
package gcu.mpd.mpdstartercode20192020;

public class Roadworks {

        private String title;
        private String description;
        private String link;

        public Roadworks()
        {
            title = "";
            description = "";
            link = "";
        }

        public Roadworks(String title1,String description1,String link1)
        {
            title = title1;
            description = description1;
            link = link1;
        }

        public String getLink()
        {
            return link;
        }

        public void setLink(String link1)
        {
            link = link1;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description1)
        {
            description = description1;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title1)
        {
            title = title1;
        }

        public String toString()
        {
            String temp;

            temp = title + "\n" + description + "\n" + link + "\n" + "\n";

            return temp;
        }


    } // End of class

