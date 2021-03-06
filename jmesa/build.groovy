/* The JMesa build. Works best with Groovy 1.1. */

class Build {
    def ant = new AntBuilder()
    def ivy = new AntLibHelper(ant:ant, namespace:'fr.jayasoft.ivy.ant')

    def projectDir = '.'

    def resourcesDir = "${projectDir}/resources"
    def targetDir = "${projectDir}/target"

    def classesDir = "${targetDir}/classes"
    def libDir = "$targetDir/ivy/lib"
    def distDir = "${targetDir}/dist"
    def docsDir = "${targetDir}/docs"

    def testDir = "${targetDir}/test"
    def testClassesDir = "${testDir}/classes"
    def testDataDir = "${testDir}/data"

    def artifact
    def zipDir

    def sourceFilesTocopy = '**/*.properties,**/*.xml'

    Build(clean) {
        if (!clean) {
            this.artifact = ['jmesa'] as Artifact
            ant.input(message:'Enter a revision number:', addproperty:'revision', defaultvalue:'snapshot')
            artifact.revision = ant.antProject.properties."revision"
            this.zipDir = "${distDir}/${artifact.name}-${artifact.revision}"
        }
    }

    def clean() {
        ant.delete(dir:targetDir)
    }

    def init() {
        ant.mkdir(dir:targetDir)
        ant.mkdir(dir:classesDir)
        ant.mkdir(dir:docsDir)
        ant.mkdir(dir:zipDir)
        ant.mkdir(dir:"${zipDir}/dist")
        ant.mkdir(dir:"${zipDir}/images")
        ant.mkdir(dir:"${zipDir}/source")
    }

    def testInit() {
        ant.mkdir(dir:testDir)
        ant.mkdir(dir:testClassesDir)
        ant.mkdir(dir:testDataDir)
    }

    def classpaths() {
        ant.path(id:'compile.classpath') {
            fileset(dir:"$libDir/compile", includes:'*.jar')
        }
    }

    def testClasspaths() {
        ant.path(id:'test.compile.classpath') {
            fileset(dir:"$libDir/test", includes:'*.jar')
            pathelement(location:artifact.file)
        }

        ant.path(id:'test.classpath') {
            path(refid:"test.compile.classpath")
            pathelement(location:testClassesDir)
        }
    }

    def compile() {
        ant.echo(message:'You are using java version ${java.version}')

        ant.javac(destdir:classesDir, srcdir:"$projectDir/src", debug:true, source:'1.5', target:'1.5', includeantruntime:false) {
            classpath(refid:'compile.classpath')
        }

        ant.copy(todir:classesDir) {
            fileset(dir:"$projectDir/src", includes:sourceFilesTocopy)
        }
    }

    def testCompile() {
        ant.echo(message:'You are using java version ${java.version}')

        ant.javac(destdir:testClassesDir, srcdir:"$projectDir/test", debug:'true', includeantruntime:false) {
            classpath(refid:'test.compile.classpath')
        }

        ant.copy(todir:testClassesDir) {
            fileset(dir:"$projectDir/test", includes:sourceFilesTocopy)
        }
    }

    def jar() {

        ant.copy(todir:classesDir + '/META-INF', file:resourcesDir + '/jmesa.tld')

        def jarFile = "$targetDir/${artifact.name}-${artifact.revision}.jar"
        ant.jar(destfile:jarFile) {
            fileset(dir:classesDir)
        }

        artifact.ext = 'jar'
        artifact.file = jarFile
    }

    def copy() {
        ant.copy(todir:zipDir + '/source') { fileset(dir:'src') }
        ant.copy(todir:zipDir + '/images') { fileset(dir:resourcesDir + '/images') }
        ant.copy(todir:zipDir + '/dist', file:targetDir + "/${artifact.name}-${artifact.revision}.jar")
        ant.copy(todir:zipDir + '/dist', file:retroDir + "/${artifact.name}-${artifact.revision}-retro.jar", failonerror: false)
        ant.copy(todir:zipDir + '/dist', file:resourcesDir + '/jquery.jmesa.js')
        ant.copy(todir:zipDir + '/dist', file:resourcesDir + '/jquery.jmesa.min.js', failonerror: false)
        ant.copy(todir:zipDir + '/dist', file:resourcesDir + '/jmesa.js')
        ant.copy(todir:zipDir + '/dist', file:resourcesDir + '/jmesa.min.js', failonerror: false)
        ant.copy(todir:zipDir + '/dist', file:resourcesDir + '/jmesa.css')
        ant.copy(todir:zipDir + '/dist', file:resourcesDir + '/jmesa-pdf.css')
        ant.copy(todir:zipDir + '/dist', file:resourcesDir + '/jmesa.tld')
    }

    def zip() {
        ant.zip(destfile:targetDir + "/${artifact.name}-${artifact.revision}.zip", basedir:distDir)
    }

    def ivyresolve() {
        ivy.configure(file:"$projectDir/ivyconf.xml")
        ivy.resolve(file:"$projectDir/ivy.xml")
    }

    def ivyretrieve() {
        ivy.retrieve(pattern:"$libDir/[conf]/[artifact]-[revision].[ext]", sync:true)
    }

    def ivypublish() {
        ivy.publish(resolver:'local',
                conf:'master',
                pubrevision:"${artifact.revision}",
                overwrite:'true',
                artifactspattern:"${targetDir}/[artifact]-[revision].[ext]")
    }

    def docs() {
        ant.javadoc(sourcepath:'src',
                destdir:docsDir,
                windowtitle:'JMesa',
                additionalparam:'-breakiterator',
                source:"1.5",
                linksource:"true",
                access:"package",
                author:"true",
                version:"true",
                use:"true",
                defaultexcludes:"true") {
            doctitle('<![CDATA[<h1>JMesa</h1>]]>')
            classpath(refid:"compile.classpath")
            packageset(dir:"src") {
                include(name:"org/jmesa/**")
            }
            link(href:"http://java.sun.com/j2se/1.5.0/docs/api")
        }
    }

    def junit() {
        ant.junit(printsummary:'no',
                errorproperty:'test.error',
                failureproperty:'test.failed',
                fork:'true',
                haltonerror:'true',
                haltonfailure:'true',) {

            formatter(type:"brief", usefile:"false")
            classpath(refid:"test.classpath")
            batchtest(todir:testDataDir, haltonerror:"true", haltonfailure:"true") {
                fileset(dir:testClassesDir, includes:"**/*Test.class")
            }
        }

        ant.fail(if:"test.failed", message:"Unit tests failed.")
        ant.fail(if:"test.error", message:"Unit tests failed with Errors.")
    }

    /*
     * Retrieve the dependant jars from the ivy repository and put
     * them in the lib folder of the project
     */
    def lib() {
        def libDir = 'lib'
        ant.mkdir(dir:libDir)
        ivy.configure(file:'ivyconf.xml')
        ivy.resolve(file:'ivy.xml')
        ivy.retrieve(pattern:"$libDir/[artifact]-[revision].[ext]", sync:true, conf:'test')
    }

    def dist() {
        clean()
        init()
        testInit()
        ivyresolve()
        ivyretrieve()
        classpaths()
        compile()
        jar()
        ivypublish()
        testClasspaths()
        testCompile()
        junit()
        retro()
        copy()
        zip()
        docs()
    }

    def retroDir = "${targetDir}/retro"
    def retroClassesDir = "${retroDir}/classes";
    def jreDir

    def retro() {
        if (!retroJreDir()) {
            return;
        }

        ant.path(id: 'retro.compiler.classpath') {
            fileset(dir:"$jreDir/lib", includes:'*.jar')
        }

        ant.path(id: 'compiled.classes') {
            pathelement(path: classesDir)
        }

        ant.taskdef(
            name: "retro",
            classname: "org.jboss.ant.tasks.retro.Retro",
            classpathref: "compile.classpath"
        );

        ant.delete(dir: retroClassesDir, failonerror: "false")
        ant.mkdir(dir: retroClassesDir);

        ant.retro(
                compilerclasspathref: "retro.compiler.classpath",
                destDir: retroClassesDir) {
            classpath(refid: "compiled.classes")
            classpath(refid: "compile.classpath")
            src(path: classesDir)
        };

        ant.copy(todir:retroClassesDir) {
            fileset(dir:"$projectDir/src", includes:sourceFilesTocopy)
        }

        ant.copy(todir: retroClassesDir + '/META-INF', file:resourcesDir + '/jmesa.tld')

        def jarFile = "$retroDir/${artifact.name}-${artifact.revision}-retro.jar"
        ant.jar(destfile:jarFile) {
            fileset(dir:retroClassesDir)
        }
    }

    def retroJreDir() {
        if (!jreDir) {
            ant.echo(message: "Please select the base directory of a 1.4 JRE for the retro jar:")
            def chooser = new javax.swing.JFileChooser()
            chooser.fileSelectionMode = javax.swing.JFileChooser.DIRECTORIES_ONLY
            chooser.multiSelectionEnabled = false
            chooser.showDialog(null, "Use as JRE 1.4 Home")
            jreDir = chooser.selectedFile
            if (jreDir) {
                ant.echo(message: "Note: To automate the retro transformation, you can specify the JDK 1.4 base directory on the command line:\nex: groovy build.groovy -a dist -j \"" + jreDir + "\"");
            }
        }

        if (!jreDir) {
            ant.echo(message: "A JRE 1.4 base directory is required for the retro jar.")
            return false
        }

        if (!jreDir.exists()) {
            ant.echo(message: "The specified JRE 1.4 base directory does not exist and is required for the retro jar!")
            return false
        }

        if (!jreDir.isDirectory()) {
            ant.echo(message: "The specified JRE 1.4 base directory is not a directory and is required for the retro jar!")
            return false
        }

        return true
    }

    static void main(args) {
        def cli = new CliBuilder(usage:'groovy build.groovy -[ha] [-j]')
        cli.h(longOpt: 'help', 'usage information')
        cli.a(longOpt:'action', args:1, required:true, 'action(s) [dist, clean, lib]')
        cli.j(longOpt: 'jrehome', args:1, required: false, 'the JDK 1.4 base directory for to link against for backporting')

        def options = cli.parse(args)

        if (options.h) {
            cli.usage()
            return
        }

        def build = new Build(options.a == 'clean' || options.a == 'lib')
        build.jreDir = options.j ? new java.io.File(options.j) : null
        def action = options.a
        build.invokeMethod(action, null)
    }
}

class AntLibHelper {
    def namespace
    def ant

    Object invokeMethod(String name, Object params) {
        ant."antlib:$namespace:$name"(*params)
    }
}

class Artifact {
    def name
    def revision
    def ext
    def file

    Artifact(String name) {
        this.name = name
    }
}
