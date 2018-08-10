# Creating a Release

## Release workflow

As release workflow we use the Gitflow Workflow (see https://www.atlassian.com/git/tutorials/comparing-workflows#gitflow-workflow).

For releasing a new version of this project, we integrated the <code>jgitflow-maven-plugin</code> (see https://bitbucket.org/atlassian/jgit-flow/wiki/Home).

Steps to build a new version:

1. Checkout branch <code>development</code>.
2. Run <code>mvn install -Pwith-app</code>.
3. If the build was successful, run <code>mvn jgitflow:release-start jgitflow:release-finish -Pwith-app,with-dep-poms</code>.
	- You will be asked for the release version. Leave this empty to keep the current snapshot version as release version number (<code>-SNAPSHOT</code> will be cut of by jgitflow-maven-plugin).
	- You will be asked for the next development version. Leave this empty and the plugin will increment the micro release number (<code>1.0.1-SNAPSHOT</code> becomes <code>1.0.2-SNAPSHOT</code>). If you want to alter the version just type e.g. <code>1.1.0-SNAPSHOT</code>.

4. The result is that the current state of branch <code>development</code> gets merged to branch <code>master</code> (without -SNAPSHOT), tagged as <code>releases/version-1.0.1</code> and the development version is automatically increased.

### Version numbers

Version Numbers = major.minor.micro

For correct generation of android version codes the releases have to be at least minor releases. Micro releases are reserved for hotfixes of a published release.
- Finish of a release: Increase major or minor number
- Finish of a hotfix (merged directly back to branch <code>master</code>): Increase micro number

### Android Version Code

The <code>versionCode</code> for the Android app is autogenerated by the projects version.
The convention for the versionCode <code>aaabbbccd</code> (generated of version <code>aaa.bbb.cc</code>) is:
  - d: one digit for SNAPSHOT (0), Release Candidates (RC1 to RC8 = 1..8) or Final Release (9)
  - cc: two digits for micro releases (with leading zeros)
  - bbb: three digits for minor releases (with leading zeros)
  - aaa: major releases (if a > 0)


### Local configuration for jgitflow-maven-plugin

The <code>jgitflow-maven-plugin</code> needs credentials for git, which are configurated as variables in <code>sormas-base/pom.xml</code>. 
To use it you need to configure this in your .m2/settings.xml (or pass it as arguments when executing the plugin).

        <profiles>
                <profile>
                        <id>github-config</id>
                        <!-- For jgitflow-maven-plugin against github.com -->
                        <properties>
                                <github.sormas.user>myUserName</github.sormas.user>
                                <github.sormas.password>myPassword</github.sormas.password>
                        </properties>
                </profile>
        </profiles>

        <activeProfiles>
                <activeProfile>github-config</activeProfile>
        </activeProfiles>