package be.fkgent.election.controllers.forms.converter;

public interface Converter<Target, Source> {
    Target update(Target target, Source source);
    Target build( Source source);
}
