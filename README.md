# ESE2013 Team 5

Project: **Mensa@Unibe**

#### Possible App Names

Add your username behind the app name of your choice in parenthesis as your vote for the app name, e.g. Name (exside)

- UniBEMensa
- Mensa@UniBE (Illuminatic)
- MensApp (Illuminatic)
- Mensa UniBE



## Taskpool

Let's have a To-Do list with individual tasks that can be assignes to team members
- (nicolas) Update of SRS to be compatible with the new navigation
- ~~(luk,raul) Prototype / Wireframe v1~~
- ~~(luk) Organize API Key for mensa REST API (mail sent)~~
- (all) Register at trello.com for SCRUM Tool
- ~~(nicolas) Navigation Diagram~~
- (all) Discuss requirements
- ~~(luk) Setup of the project organization~~


## Organization

**What do we need?**

- A common GitHub commit/merge strategy, PLEASE READ THE INSTRUCTIONS BELOW!!!
- Requirements
- Prototype / Wireframe


## Workflow

### GitHub
1. Each team member forks the original repository to his own account to work on it
2. Clone the forked repository to your local machine
3. Work on the project, add and commit the changes to your local repository
4. Push the changes online to your forked master branch
5. Make a pull request against the original ese2013-team5 repositorys master branch (in the online interface via the green button)
6. Pull request should be merged only, if all team members have commented with +1 to give their vote, if this is the case the responsible for the pull request can merge the change into the projects master branch
7. Do the merging of pull request in the original repository via the web frontend, so nobody would have to fork the "real/original" repository and thus maybe make unintentional changes
8. For faster testing, it would be very handy if everybody could compile an actual .apk package that can be tested on a real device!

#### Synchronizing the original repository with your fork
1. Terminal > cd to local project directory on your computer, check with `git status` that you are in the right directory!
2. Make an alias of the original project `git remote add original https://github.com/ese-unibe-ch/ese2013-team5.git`
3. Get the files from the original repository to your local repository with `git fetch original`
4. Merge the original with the local repository `git merge original/master` (it will open vim, just hit ESC, then type :w, then :q, the standard message is fine)
5. Push the changes to your own online repository/fork `git push origin master`

#### Normal GitHub commit workflow
1. make the changes in the code
2. Terminal > cd to local repository if not there yet (you should probably have the most recent version synced before starting to work on it)
3. `git status` to see what's the current status of that repository
4. `git add <file>` or `git add *` (for all files) to add the changed files to a commit (seen via `git status`)
5. `git commit -m "<commit message>"` to commit the added changes and set the message that will appear on GitHub
6. `git push origin master` to upload the changes to the master branch of your online repository/fork on GitHub
7. go to your own online repository/fork and click the green "review/compare" button left to the branch dropdown
8. create a pull request against the original repository
9. you are taken to the original repository, if the change doesn't need approval of all team members you can merge your pull request on the presented page

### Eclipse
- Do not use your workspace project folder as the git repository! It will be a mess...if you have made and tested your changes to the code, copy the project files into your github repositories folder -> ProjectFiles
- We want a multilanguage app, all strings should always be translated to german and english in their respecitve folders (res/values for english, res/values-de for german)

## Knowledge / Links

- How to use GitHub, an [introduction](http://rogerdudler.github.io/git-guide/index.de.html)
- [GitHub Markdown Cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)
- [Project Description / API from Wiki](https://github.com/ese-unibe-ch/ese2013-wiki/wiki/Project-Mensa@Unibe)
- [Mensa Webservice / REST API](https://github.com/lexruee/Mensa-Webservice)
- [Trello SCRUM Plattform](https://trello.com/b/pS8FuRWx/team-5-mensa-unibe)
