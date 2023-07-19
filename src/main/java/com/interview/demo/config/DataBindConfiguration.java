//package com.interview.demo.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
//import com.qburger.entity.QAddon;
//import com.qburger.entity.QProduct;
//import com.qburger.entity.QSpec;
//import com.qburger.entity.Unit;
//import com.qburger.entity.type.UnitType;
//import com.qburger.model.product.AddonDto;
//import com.qburger.model.product.BriefProduct;
//import com.qburger.model.product.SpecDto;
//import com.qburger.model.product.UnitDto;
//import com.qburger.repository.querydsl.QuerydslRepository;
//import io.swagger.v3.core.util.DeserializationModule;
//import io.swagger.v3.oas.models.media.Schema;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.convention.MatchingStrategies;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//
//import java.util.stream.Collectors;
//
//@Configuration
//public class DataBindConfiguration {
//
//    @Bean
//    public ModelMapper modelMapper(ObjectMapper objectMapper, QuerydslRepository queryCtx) {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.STRICT)
//                .setAmbiguityIgnored(true);
//
//        // Unit -> UnitDto
//        modelMapper
//                .createTypeMap(Unit.class, UnitDto.class)
//                .setPostConverter(ctx -> {
//                    UnitDto dest = ctx.getDestination();
//                    Unit src = ctx.getSource();
//                    if (src.getType() == UnitType.PRODUCT) {
//                        dest.setProduct(queryCtx.newQuery()
//                                .selectFrom(QProduct.product)
//                                .where(QProduct.product.unitId.eq(src.getId()))
//                                .fetch()
//                                .stream()
//                                .map(x -> modelMapper.map(x, BriefProduct.class))
//                                .findFirst()
//                                .orElse(null));
//                    } else if (src.getType() == UnitType.SPEC) {
//                        dest.setSpec(queryCtx.newQuery()
//                                .selectFrom(QSpec.spec)
//                                .where(QSpec.spec.unitId.eq(src.getId()))
//                                .fetch()
//                                .stream()
//                                .map(x -> modelMapper.map(x, SpecDto.class))
//                                .findFirst()
//                                .orElse(null));
//                    } else if (src.getType() == UnitType.ADDON) {
//                        dest.setAddon(queryCtx.newQuery()
//                                .selectFrom(QAddon.addon)
//                                .where(QAddon.addon.unitId.eq(src.getId()))
//                                .fetch()
//                                .stream()
//                                .map(x -> modelMapper.map(x, AddonDto.class))
//                                .findFirst()
//                                .orElse(null));
//                    }
//                    return dest;
//                });
//        return modelMapper;
//    }
//
//    @Bean
//    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
//        return new Jackson2ObjectMapperBuilder() {
//
//            @Override
//            public void configure(ObjectMapper objectMapper) {
//                super.configure(objectMapper);
//                // using ISO8601 datetime format
//                objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//                objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
//                objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.NONE);
//                objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.PUBLIC_ONLY);
//                objectMapper.setVisibility(PropertyAccessor.IS_GETTER, Visibility.PUBLIC_ONLY);
//                objectMapper.registerModule(new Jdk8Module());
//            }
//
//        };
//    }
//
//}
