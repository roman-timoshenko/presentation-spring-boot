package tk.shold.software.java.presentationspringboot.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.shold.software.java.presentationspringboot.model.Rule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Component
@Log
public class ScheduleService {

    private final RulesService rulesService;
    private final DirectoryParserService directoryParserService;

    private Rule lastRule = null;
    private long endOfCurrentPresentation = new Date().getTime();

    @Autowired
    public ScheduleService(RulesService rulesService, DirectoryParserService directoryParserService) {
        this.rulesService = rulesService;
        this.directoryParserService = directoryParserService;
    }

    private Rule getTimeMatchingRule(List<Rule> rules, Date time) {
        //log.info(time.toString());

        return rules.stream()
                .filter(r -> r.getStart().getTime() <= time.getTime() && r.getEnd().getTime() >= time.getTime())
                .max(Comparator.comparingInt(Rule::getPriority))
                .orElseThrow(() -> new RuntimeException("There is no rule for this!"));
    }

    private List<String> getFilesMatchingRule(Rule rule) {
        /*List<String> folders = getDirsMatchingRule(rule);
        Random rand = new Random();
        int random = rand.nextInt(folders.size());
        List<String> result = directoryParserService.getFileInDir(folders.get(random));
        if (result.size() > 0) return result;
        return directoryParserService.getFileInDir(rule.getFolder());
        */
        return getRandomFilesMatchingRule(rule);
    }

    private List<String> getRandomFilesMatchingRule(Rule rule)
    {
        Random rand = new Random();
        List<String> result = new ArrayList<>();
        List<String> folders = getDirsMatchingRule(rule);
        List<String> temp = new ArrayList<>();
        while (temp.size()< folders.size())
        {
            int random = rand.nextInt(folders.size());
            if (!temp.contains(folders.get(random)))
            {
                log.info("" + random + " = " + folders.get(random));
                result.addAll(directoryParserService.getFileInDir(folders.get(random)));
                temp.add(folders.get(random));
            }
        }
        return result;
    }

    private List<String> getDirsMatchingRule(Rule rule) {
        return directoryParserService.getSubfoldersInDir(rule.getFolder());
    }

    private Date getTimeFromStartDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+03"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return new Date(new Date().getTime() - calendar.getTimeInMillis());
    }



    @Scheduled(fixedRate = 1000)
    public void tick() throws IOException {
        int screenDelay = 3;
        int blend = 1000;
        final List<Rule> rules = rulesService.getRules();
        final Date currentTime = getTimeFromStartDay();
        final Rule matchingRule = getTimeMatchingRule(rules, currentTime);
        if (!matchingRule.equals(lastRule) || endOfCurrentPresentation < new Date().getTime()) {
            log.info("Rules: " + matchingRule + " | " + lastRule);
            //log.info("Let's send following files: " + getFilesMatchingRule(matchingRule));
            //log.info("we have next subfolders there: " + getDirsMatchingRule(matchingRule));
            List<String> currentPresentation = getFilesMatchingRule(matchingRule);
            Runtime.getRuntime().exec("killall fbi");
            try
            {
                Thread.sleep(10L);
            }
            catch (InterruptedException e)
            {
                log.warning(e.getMessage());
            }
            //"--blend", "1000",
            List<String> cmd_temp = Arrays.asList("fbi", "--autodown", "--blend", Integer.toString(blend), "--norandom", "-a", "-T", "1", "-t", Integer.toString(screenDelay));
            List<String> cmd = new ArrayList<>();
            cmd.addAll(cmd_temp);
            cmd.addAll(currentPresentation);
            endOfCurrentPresentation = new Date().getTime() + currentPresentation.size()*screenDelay*1000+currentPresentation.size()*blend+currentPresentation.size()*1000;
            log.info("Сейчас " + new Date() + " Презентация закончится в " + new Date(endOfCurrentPresentation).toString());
            try
            {
                runPresentation(cmd);
            }
            catch (IOException e)
            {
                log.warning(e.getMessage());
            }
            lastRule = matchingRule;
        }
    }

    private void runPresentation(List<String> cmd) throws IOException
    {
        ProcessBuilder procBuilder = new ProcessBuilder(cmd);
        System.out.println(cmd);
        procBuilder.redirectErrorStream(true);

        // запуск программы
        Process process = procBuilder.start();
        try {
            // читаем стандартный поток вывода
            // и выводим на экран
            InputStream stdout = process.getInputStream();
            InputStreamReader isrStdout = new InputStreamReader(stdout);
            BufferedReader brStdout = new BufferedReader(isrStdout);

            String line = null;
            while((line = brStdout.readLine()) != null) {
                System.out.println(line);
            }

            // ждем пока завершится вызванная программа
            // и сохраняем код, с которым она завершилась в
            // в переменную exitVal
            int exitVal = process.waitFor();
            System.out.println(exitVal);
        }
        catch (InterruptedException e)
        {
            System.err.println(e);
        }
    }

    /*
    public void reload() {
        final List<Rule> rules;
        try {
            rules = rulesService.getRules();
            log.info("Rules: " + rules);
        } catch (IOException e) {
            log.log(Level.SEVERE, e, () -> "Error reading rules");
            throw new RuntimeException(e);
        }


        final List<Displayable> displayables = formDisplayable(rules, new Date(0), new Date(86400000));
        final DisplayListTask displayListTask = new DisplayListTask(displayables);
        final DisplayListTaskTrigger displayListTaskTrigger = new DisplayListTaskTrigger(new Date(), null, displayListTask);
        taskScheduler.schedule(displayListTask, displayListTaskTrigger);

        log.info("Finished scheduling.");
    }
*/
}
