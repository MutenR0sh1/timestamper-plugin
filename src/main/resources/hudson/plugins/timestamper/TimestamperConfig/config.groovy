package hudson.plugins.timestamper.TimestamperConfig

def f=namespace(lib.FormTagLib)

f.section(title:_("Timestamper Format")) {
    f.entry(title: _("Timestamp format"), field:"timestampFormat") {
        f.textbox()
    }

    f.entry(title: _("Line Prefix"), field:"linePrefix") {
        f.textbox()
    }
}