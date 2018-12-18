//: annotations/InterfaceExtractorProcessorFactory.java
// APT-based annotation processing.
package annotations;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class InterfaceExtractorProcessorFactory implements AnnotationProcessorFactory {
    @Override
    public AnnotationProcessor getProcessorFor(
            Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env) {
        return new InterfaceExtractorProcessor(env);
    }

    @Override
    public Collection<String> supportedAnnotationTypes() {
        return Collections.singleton(ExtractInterface.class.getName());
    }

    @Override
    public Collection<String> supportedOptions() {
        return Collections.emptySet();
    }
} ///:~
